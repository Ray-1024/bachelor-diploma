<template>
  <div>
    <UserProfile :initialUser="currentUser" @profile-updated="handleProfileUpdate" />
  </div>
</template>

<script>
import UserProfile from './components/UserProfile.vue'

export default {
  components: {
    UserProfile
  },
  data() {
    return {
      currentUser: {
        name: 'John Doe',
        username: 'johndoe',
        email: 'john@example.com',
        phone: '+1234567890',
        location: 'New York, USA',
        bio: 'Frontend developer passionate about Vue.js and user experience.',
        avatar: 'https://example.com/avatar.jpg',
        joinDate: '2020-05-15T00:00:00Z',
        socialLinks: {
          twitter: 'https://twitter.com/johndoe',
          github: 'https://github.com/johndoe',
          linkedin: 'https://linkedin.com/in/johndoe'
        }
      }
    }
  },
  methods: {
    handleProfileUpdate(updatedUser) {
      // Here you would typically send to an API
      this.currentUser = updatedUser
    }
  }
}
</script>