package com.github.paicoding.forum.core.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5Util 类提供了对字符串或字节数组进行 MD5 加密的工具方法。
 * MD5 是一种广泛使用的哈希算法，用于生成数据的哈希值。
 * 该类提供了多种重载方法，可以方便地对不同类型的数据进行 MD5 哈希加密。
 *
 * 注意：MD5 已被认为不够安全，对于敏感数据（如密码）建议使用更安全的哈希算法（如 SHA-256 或 bcrypt）。
 */
public class Md5Util {

    // 私有化构造方法，防止实例化
    private Md5Util() {
    }

    /**
     * 对传入的字符串进行 MD5 编码。
     * 此方法将字符串转换为字节数组并调用 `encode(byte[] bytes)` 方法进行加密。
     *
     * @param data 需要加密的字符串
     * @return 返回计算后的 MD5 值（16进制字符串）
     */
    public static String encode(String data) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        return encode(bytes);
    }

    /**
     * 对字节数组进行 MD5 编码。
     * 此方法将字节数组传入加密逻辑并返回 MD5 的 16 进制表示。
     *
     * @param bytes 需要加密的字节数组
     * @return 返回计算后的 MD5 值（16进制字符串）
     */
    public static String encode(byte[] bytes) {
        return encode(bytes, 0, bytes.length);
    }

    /**
     * 对字节数组的指定部分进行 MD5 编码。
     * 允许对字节数组的指定偏移量和长度进行加密，适用于需要部分加密的情况。
     *
     * @param data   需要加密的字节数组
     * @param offset 起始偏移量
     * @param len    加密的数据长度
     * @return 返回计算后的 MD5 值（16进制字符串）
     */
    public static String encode(byte[] data, int offset, int len) {
        MessageDigest md;
        try {
            // 获取 MD5 加密算法的实例
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var5) {
            // 如果没有找到 MD5 算法，抛出运行时异常
            throw new RuntimeException(var5);
        }

        // 更新数据
        md.update(data, offset, len);
        byte[] secretBytes = md.digest(); // 计算 MD5 值
        return getFormattedText(secretBytes); // 返回格式化后的 MD5 值
    }

    /**
     * 将字节数组转换为 16 进制的字符串表示。
     * 这个方法用于将计算得到的 MD5 字节数组转为标准的 32 位 16 进制字符串格式。
     *
     * @param src 字节数组（MD5 结果）
     * @return 16 进制字符串
     */
    private static String getFormattedText(byte[] src) {
        if (src != null && src.length != 0) {
            StringBuilder stringBuilder = new StringBuilder(32);

            // 遍历字节数组，将每个字节转换为 16 进制字符串
            for (int i = 0; i < src.length; ++i) {
                int v = src[i] & 255; // 获取低 8 位
                String hv = Integer.toHexString(v); // 转换为 16 进制
                if (hv.length() < 2) {
                    stringBuilder.append(0); // 补充零
                }

                stringBuilder.append(hv); // 拼接
            }

            return stringBuilder.toString();
        } else {
            return "";
        }
    }
}
