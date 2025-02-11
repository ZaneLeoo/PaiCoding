<template>
  <div class="container">
    <user-profile></user-profile>
    <user-articles></user-articles>
  </div>
</template>
<script>
import UserProfile from '@/components/user/UserProfile.vue'
import UserArticles from '@/components/user/UserArticles.vue'
export default {
  components: {
    UserProfile,
    UserArticles
  }
}
</script>

<style scoped>
.container {
  display: flex;
}
</style>
