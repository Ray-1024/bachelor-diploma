<template>
  <article class="article">
    <header class="article-header">
      <h1 class="article-title">{{ title }}</h1>

      <div v-if="meta" class="article-meta">
        <span v-if="meta.author" class="article-author">
          By {{ meta.author }}
        </span>
        <span v-if="meta.date" class="article-date">
          {{ formatDate(meta.date) }}
        </span>
        <span v-if="meta.readingTime" class="article-reading-time">
          {{ meta.readingTime }} min read
        </span>
      </div>

      <img
          v-if="featuredImage"
          :src="featuredImage"
          :alt="featuredImageAlt || 'Featured image'"
          class="article-featured-image"
      >
    </header>

    <div class="article-content" v-html="content"></div>

    <footer v-if="tags && tags.length" class="article-footer">
      <div class="article-tags">
        <span v-for="tag in tags" :key="tag" class="article-tag">
          {{ tag }}
        </span>
      </div>
    </footer>
  </article>
</template>

<script>
export default {
  name: 'Article',
  props: {
    title: {
      type: String,
      required: true
    },
    content: {
      type: String,
      required: true
    },
    meta: {
      type: Object,
      default: () => ({
        author: null,
        date: null,
        readingTime: null
      })
    },
    featuredImage: {
      type: String,
      default: null
    },
    featuredImageAlt: {
      type: String,
      default: null
    },
    tags: {
      type: Array,
      default: () => []
    }
  },
  methods: {
    formatDate(date) {
      if (!date) return '';

      // You can replace this with a more sophisticated date formatting library
      // like date-fns or moment.js if needed
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(date).toLocaleDateString(undefined, options);
    }
  }
}
</script>

<style scoped>
.article {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
  line-height: 1.6;
  color: #333;
}

.article-header {
  margin-bottom: 2rem;
}

.article-title {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  line-height: 1.2;
}

.article-meta {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
  color: #666;
  font-size: 0.9rem;
}

.article-featured-image {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 1.5rem;
}

.article-content {
  font-size: 1.1rem;
}

.article-content >>> p {
  margin-bottom: 1.5rem;
}

.article-content >>> h2 {
  font-size: 1.8rem;
  margin: 2rem 0 1rem;
}

.article-content >>> h3 {
  font-size: 1.5rem;
  margin: 1.5rem 0 0.75rem;
}

.article-content >>> a {
  color: #0066cc;
  text-decoration: underline;
}

.article-content >>> img {
  max-width: 100%;
  height: auto;
  margin: 1rem 0;
}

.article-footer {
  margin-top: 3rem;
  padding-top: 1.5rem;
  border-top: 1px solid #eee;
}

.article-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.article-tag {
  background: #f0f0f0;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
  color: #555;
}
</style>