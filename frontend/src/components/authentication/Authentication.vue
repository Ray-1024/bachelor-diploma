<script setup lang="ts">

import {client} from "@/client/client";
import {ref} from "vue";

const isLoginMode = ref(true);
const isSubmitting = ref(false);
const apiError = ref(null);
const form = ref({
  username: '',
  password: ''
});
const errors = ref({
  username: '',
  password: ''
});

function validateField(field: string) {
  if (field === 'username') {
    if (!form.value.username) {
      errors.value.username = 'Username is required';
    } else {
      errors.value.username = null;
    }
  }

  if (field === 'password') {
    if (!form.value.password) {
      errors.value.password = 'Password is required';
    } else if (form.value.password.length < 6) {
      errors.value.password = 'Password must be at least 6 characters';
    } else {
      errors.value.password = null;
    }
  }
}

function validateForm() {
  validateField('username')
  validateField('password')
  return !errors.value.username && !errors.value.password && (isLoginMode);
}

function handleSubmit() {

  client.auth.token = '1';

  if (!validateForm()) {
    return
  }

  isSubmitting.value = true
  apiError.value = null

  try {
    if (isLoginMode.value) {
      client.auth.signIn(form.value.username, form.value.password)
    } else {
      client.auth.signUp(form.value.username, form.value.password)
    }
  } catch (error) {
    apiError.value = error.response?.data?.message || 'An error occurred. Please try again.'
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2>{{ isLoginMode ? 'Sign In' : 'Sign Up' }}</h2>

      <form @submit.prevent="handleSubmit" class="auth-form">

        <div class="form-group">
          <label for="username">Username</label>
          <input
              type="text"
              id="username"
              v-model="form.username"
              @blur="validateField('username')"
              :class="{ 'invalid': errors.username }"
          />
          <span class="error-message" v-if="errors.username">{{ errors.username }}</span>
        </div>

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

        <button type="submit" :disabled="isSubmitting" class="submit-btn">
          {{ isSubmitting ? 'Processing...' : isLoginMode ? 'Sign In' : 'Sign Up' }}
        </button>

        <p class="toggle-mode">
          {{ isLoginMode ? "Don't have an account?" : "Already have an account?" }}
          <a href="#" @click.prevent="isLoginMode = !isLoginMode">{{ isLoginMode ? 'Sign Up' : 'Sign In' }}</a>
        </p>

        <div class="api-error" v-if="apiError">
          {{ apiError }}
        </div>
      </form>
    </div>
  </div>
</template>

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
</style>