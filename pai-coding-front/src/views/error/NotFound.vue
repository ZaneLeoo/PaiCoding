<template>
  <a-result status="404">
    <template #extra>
      <h2>404</h2>
      <p>你似乎迷路了!靓仔😶‍🌫️</p>
      <a-button type="primary">
        <router-link to="/">Back Home</router-link>
      </a-button>
    </template>
  </a-result>
</template>
<style scoped>
p {
  font-size: 16px;
  font-family: 'Ubuntu', sans-serif;
}
</style>
