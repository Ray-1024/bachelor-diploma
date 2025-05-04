<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2>{{ isLoginMode ? 'Sign In' : 'Sign Up' }}</h2>

      <form @submit.prevent="handleSubmit" class="auth-form">
        <!-- Email Field -->
        <div class="form-group">
          <label for="email">Email</label>
          <input
              type="email"
              id="email"
              v-model="form.email"
              @blur="validateField('email')"
              :class="{ 'invalid': errors.email }"
          />
          <span class="error-message" v-if="errors.email">{{ errors.email }}</span>
        </div>

        <!-- Password Field -->
        <div class="form-group">
          <label for="password">Password</label>
          <input
              type="password"
              id="password"
              v-model="form.password"
              @blur="validateField('password')"
              :class="{ 'invalid': errors.password }"
          />
          <span class="error-message" v-if="errors.password">{{ errors.password }}</span>
        </div>

        <!-- Name Field (only for sign up) -->
        <div class="form-group" v-if="!isLoginMode">
          <label for="name">Full Name</label>
          <input
              type="text"
              id="name"
              v-model="form.name"
              @blur="validateField('name')"
              :class="{ 'invalid': errors.name }"
          />
          <span class="error-message" v-if="errors.name">{{ errors.name }}</span>
        </div>

        <!-- Submit Button -->
        <button type="submit" :disabled="isSubmitting" class="submit-btn">
          {{ isSubmitting ? 'Processing...' : isLoginMode ? 'Sign In' : 'Sign Up' }}
        </button>

        <!-- Toggle between Sign In/Sign Up -->
        <p class="toggle-mode">
          {{ isLoginMode ? "Don't have an account?" : "Already have an account?" }}
          <a href="#" @click.prevent="toggleMode">{{ isLoginMode ? 'Sign Up' : 'Sign In' }}</a>
        </p>

        <!-- Error message for API errors -->
        <div class="api-error" v-if="apiError">
          {{ apiError }}
        </div>
      </form>

      <!-- Social login options -->
      <div class="social-login" v-if="isLoginMode">
        <p class="divider">or continue with</p>
        <div class="social-buttons">
          <button @click="socialLogin('google')" class="social-btn google">
            <i class="fab fa-google"></i> Google
          </button>
          <button @click="socialLogin('facebook')" class="social-btn facebook">
            <i class="fab fa-facebook-f"></i> Facebook
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AuthForm',
  data() {
    return {
      isLoginMode: true,
      isSubmitting: false,
      apiError: null,
      form: {
        email: '',
        password: '',
        name: ''
      },
      errors: {
        email: null,
        password: null,
        name: null
      }
    }
  },
  methods: {
    toggleMode() {
      this.isLoginMode = !this.isLoginMode
      this.resetForm()
      this.apiError = null
    },
    resetForm() {
      this.form = {
        email: '',
        password: '',
        name: ''
      }
      this.errors = {
        email: null,
        password: null,
        name: null
      }
    },
    validateField(field) {
      if (field === 'email') {
        if (!this.form.email) {
          this.errors.email = 'Email is required'
        } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.form.email)) {
          this.errors.email = 'Please enter a valid email'
        } else {
          this.errors.email = null
        }
      }

      if (field === 'password') {
        if (!this.form.password) {
          this.errors.password = 'Password is required'
        } else if (this.form.password.length < 6) {
          this.errors.password = 'Password must be at least 6 characters'
        } else {
          this.errors.password = null
        }
      }

      if (field === 'name' && !this.isLoginMode) {
        if (!this.form.name) {
          this.errors.name = 'Name is required'
        } else if (this.form.name.length < 2) {
          this.errors.name = 'Name must be at least 2 characters'
        } else {
          this.errors.name = null
        }
      }
    },
    validateForm() {
      this.validateField('email')
      this.validateField('password')
      if (!this.isLoginMode) {
        this.validateField('name')
      }

      return !this.errors.email && !this.errors.password && (this.isLoginMode || !this.errors.name)
    },
    async handleSubmit() {
      if (!this.validateForm()) {
        return
      }

      this.isSubmitting = true
      this.apiError = null

      try {
        if (this.isLoginMode) {
          // Call your sign in API
          await this.$store.dispatch('auth/login', {
            email: this.form.email,
            password: this.form.password
          })
          this.$router.push('/dashboard') // Redirect after login
        } else {
          // Call your sign up API
          await this.$store.dispatch('auth/register', {
            name: this.form.name,
            email: this.form.email,
            password: this.form.password
          })
          this.$router.push('/welcome') // Redirect after registration
        }
      } catch (error) {
        this.apiError = error.response?.data?.message || 'An error occurred. Please try again.'
      } finally {
        this.isSubmitting = false
      }
    },
    socialLogin(provider) {
      // Redirect to social login endpoint or handle with a popup
      window.location.href = `/api/auth/${provider}`
    }
  }
}
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20px;
}

.auth-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 2rem;
  width: 100%;
  max-width: 400px;
}

h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #333;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

label {
  font-weight: 500;
  color: #555;
}

input {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

input.invalid {
  border-color: #ff4444;
}

.error-message {
  color: #ff4444;
  font-size: 0.85rem;
  margin-top: 0.25rem;
}

.submit-btn {
  background-color: #4285f4;
  color: white;
  padding: 0.75rem;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  margin-top: 1rem;
  transition: background-color 0.3s;
}

.submit-btn:hover {
  background-color: #3367d6;
}

.submit-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.toggle-mode {
  text-align: center;
  margin-top: 1rem;
  color: #666;
}

.toggle-mode a {
  color: #4285f4;
  text-decoration: none;
}

.toggle-mode a:hover {
  text-decoration: underline;
}

.api-error {
  color: #ff4444;
  text-align: center;
  margin-top: 1rem;
  padding: 0.5rem;
  background-color: #ffeeee;
  border-radius: 4px;
}

.social-login {
  margin-top: 2rem;
}

.divider {
  text-align: center;
  position: relative;
  color: #777;
  margin: 1rem 0;
}

.divider::before, .divider::after {
  content: "";
  position: absolute;
  top: 50%;
  width: 40%;
  height: 1px;
  background-color: #ddd;
}

.divider::before {
  left: 0;
}

.divider::after {
  right: 0;
}

.social-buttons {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

.social-btn {
  flex: 1;
  padding: 0.75rem;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  transition: opacity 0.3s;
}

.social-btn:hover {
  opacity: 0.9;
}

.social-btn.google {
  background-color: #db4437;
  color: white;
}

.social-btn.facebook {
  background-color: #4267b2;
  color: white;
}
</style>