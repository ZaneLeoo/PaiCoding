package com.github.paicoding.forum.web.controller.user.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.paicoding.forum.api.model.context.ReqInfoContext;
import com.github.paicoding.forum.api.model.enums.FollowSelectEnum;
import com.github.paicoding.forum.api.model.enums.HomeSelectEnum;
import com.github.paicoding.forum.api.model.vo.ResVo;
import com.github.paicoding.forum.api.model.vo.article.dto.ArticleDTO;
import com.github.paicoding.forum.api.model.vo.article.dto.TagSelectDTO;
import com.github.paicoding.forum.api.model.vo.constants.StatusEnum;
import com.github.paicoding.forum.api.model.vo.user.UserInfoSaveReq;
import com.github.paicoding.forum.api.model.vo.user.UserRelationReq;
import com.github.paicoding.forum.api.model.vo.user.dto.FollowUserInfoDTO;
import com.github.paicoding.forum.api.model.vo.user.dto.UserStatisticInfoDTO;
import com.github.paicoding.forum.core.permission.Permission;
import com.github.paicoding.forum.core.permission.UserRole;
import com.github.paicoding.forum.service.article.repository.entity.ArticleDO;
import com.github.paicoding.forum.service.article.service.ArticleReadService;
import com.github.paicoding.forum.service.user.cahce.UserInfoCacheManager;
import com.github.paicoding.forum.service.user.repository.entity.UserFootDO;
import com.github.paicoding.forum.service.user.service.UserFootService;
import com.github.paicoding.forum.service.user.service.relation.UserRelationServiceImpl;
import com.github.paicoding.forum.service.user.service.user.UserServiceImpl;
import com.github.paicoding.forum.web.controller.article.vo.ArticleStatusVo;
import com.github.paicoding.forum.web.controller.user.req.UserOperateReq;
import com.github.paicoding.forum.web.controller.user.vo.UserHomeInfoVo;
import com.github.paicoding.forum.web.global.vo.ResultVo;
import com.github.paicoding.forum.web.mq.EventConsumer;
import com.github.paicoding.forum.web.mq.EventProducer;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author XuYifei
 * @date 2024/7/8
 */
@RestController
@RequestMapping(path = "user/api")
public class UserRestController {

    @Resource
    private UserServiceImpl userService;

    @Resource
    private UserRelationServiceImpl userRelationService;

    @Resource
    private ArticleReadService articleReadService;

    @Resource
    private UserInfoCacheManager userInfoCacheManager;

    @Resource
    private EventProducer producer;

    private static final List<String> homeSelectTags = Arrays.asList("article", "read", "follow", "collection");
    private static final List<String> followSelectTags = Arrays.asList("follow", "fans");
    @Autowired
    private UserFootService userFootService;


    /**
     * 保存用户关系
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "saveUserRelation")
    public ResVo<Boolean> saveUserRelation(@RequestBody UserRelationReq req) {
        userRelationService.saveUserRelation(req);
        return ResVo.ok(true);
    }

    /**
     * 保存用户详情
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Permission(role = UserRole.LOGIN)
    @PostMapping(path = "saveUserInfo")
    @Transactional(rollbackFor = Exception.class)
    public ResVo<Boolean> saveUserInfo(@RequestBody UserInfoSaveReq req) {
        if (req.getUserId() == null || !Objects.equals(req.getUserId(), ReqInfoContext.getReqInfo().getUserId())) {
            // 不能修改其他用户的信息
            return ResVo.fail(StatusEnum.FORBID_ERROR_MIXED, "无权修改");
        }
        userInfoCacheManager.delUserInfo(req.getUserId());
        userService.saveUserInfo(req);
        return ResVo.ok(true);
    }

    /**
     * 获取用户主页信息，通常只有作者本人才能进入这个页面
     *
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "home")
    public ResultVo<UserHomeInfoVo> getUserHome(@RequestParam(name = "userId") Long userId) {
        UserHomeInfoVo vo = new UserHomeInfoVo();
        UserStatisticInfoDTO userInfo = userService.queryUserInfoWithStatistic(userId);
        vo.setUserHome(userInfo);
        return ResultVo.ok(vo);
    }
    /**
     * 获取用户主页的关注列表
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "follows")
    public ResultVo<IPage<FollowUserInfoDTO>> getUserFollowed(@RequestParam(name = "userId") Long userId,
                                                              @RequestParam(name = "currentPage", required = false, defaultValue = "1") int currentPage,
                                                              @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return ResultVo.ok(userRelationService.getUserFollowListPagination(userId, currentPage, pageSize));
    }

    /**
     * 获取用户主页的粉丝列表
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Permission(role = UserRole.LOGIN)
    @GetMapping(path = "fans")
    public ResultVo<IPage<FollowUserInfoDTO>> getUserFans(@RequestParam(name = "userId") Long userId,
                                                              @RequestParam(name = "currentPage", required = false, defaultValue = "1") int currentPage,
                                                              @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return ResultVo.ok(userRelationService.getUserFansListPagination(userId, currentPage, pageSize));
    }


    @PostMapping("/operate")
    public ResVo<String> userOperate(@RequestBody UserOperateReq req){
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        ArticleStatusVo vo = new ArticleStatusVo();
        req.setOperateUserId(userId);
        if (req.getUserOperateType().equals("article")){
            producer.sendLikeEvent(req.getOperateUserId(), req.getOperateEntityId(),
                    req.getOperateEntityAuthorId(),req.getOperateStatus());
        }else if (req.getUserOperateType().equals("user")){
            producer.sendFollowEvent(req.getOperateUserId(), req.getOperateEntityId(),
                    req.getOperateStatus());
        }
        return ResVo.ok("success");
    }
}
