<template>
  <header-bar></header-bar>
  <router-view></router-view>
  <login-dialog :clicked="loginDialogClicked"></login-dialog>
  <register-dialog :clicked="registerDialogClicked"></register-dialog>
</template>
<script>
import HeaderBar from './components/layout/HeaderBar.vue'
import LoginDialog from './components/dialog/LoginDialog.vue'
import RegisterDialog from './components/dialog/RegisterDialog.vue'
import { useUserStore } from '@/stores/global.js'
import Cookies from 'js-cookie'
export default {
  components: {
    HeaderBar,
    LoginDialog,
    RegisterDialog
  },
  data() {
    return {
      loginDialogClicked: false,
      registerDialogClicked: false
    }
  },
  methods: {
    changeClickedLogin() {
      this.loginDialogClicked = !this.loginDialogClicked
    },
    changeClickedRegister() {
      this.registerDialogClicked = !this.registerDialogClicked
    },
    checkLoginState() {
      const userStore = useUserStore()
      const session = Cookies.get('p-session')
      const avatar = Cookies.get('avatar')
      console.log(session)
      console.log(userStore)
      if (session) {
        userStore.isLogin = true
        userStore.avatar = avatar
      } else {
        userStore.isLogin = false
      }
    }
  },
  provide() {
    return {
      RegisterDialogClicked: this.changeClickedRegister,
      loginDialogClicked: this.changeClickedLogin
    }
  },
  mounted() {
    this.checkLoginState()
  },
  beforeRouteEnter(to, from, next) {
    this.checkLoginState()
    next()
  }
}
</script>
<style scoped>
header {
  line-height: 1.5;
  max-height: 100vh;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
  margin-top: 2rem;
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }

  nav {
    text-align: left;
    margin-left: -1rem;
    font-size: 1rem;

    padding: 1rem 0;
    margin-top: 1rem;
  }
}
</style>
