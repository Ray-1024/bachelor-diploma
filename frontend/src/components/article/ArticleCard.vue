<template>
  <Overlay>
    <template v-slot:card>
      <div class="submission-header">
        <span class="submission-title">{{ props.article.title }}</span>
        <span class="submission-language">by {{ props.article.authorUsername }}</span>
        <span class="submission-date">{{ formatDate(props.article.creationDate) }}</span>
      </div>
      <div class="submission-code">
        {{ truncateText(props.article.article, 50) }}
      </div>
      <div class="article-tags">
              <span class="tag" v-for="(tag,index) in props.article.tags" :key="index">
                {{ client.tags.getById(tag).tag }}
              </span>
      </div>
    </template>
    <template v-slot:popup>
      <Article :article="props.article" @search-problem="handleSearchProblem"/>
    </template>
  </Overlay>
</template>

<script setup lang="ts">
import {client} from "@/client/client";
import {ArticleDto, TagsDto} from "@/client/data"
import Article from "@/components/article/Article.vue";
import Overlay from "@/components/overlay/Overlay.vue";

const props = defineProps<{
  article: ArticleDto
}>();

const emit = defineEmits(['searchProblem']);

async function handleSearchProblem(params: TagsDto) {
  emit('searchProblem', params);
}

function formatDate(dateString: Date) {
  const options: Intl.DateTimeFormatOptions = {year: 'numeric', month: 'short', day: 'numeric'};
  return dateString.toLocaleDateString(undefined, options);
}

function truncateText(text: String, maxLength: number): String {
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength - 3) + '...';
}
</script>

<style scoped>
.tag {
  display: inline-block;
  background: #e0e0e0;
  padding: 4px 8px;
  border-radius: 4px;
  margin-right: 5px;
  margin-bottom: 5px;
  font-size: 12px;
}

.submission-header {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  margin-bottom: 8px;
  gap: 10px;
}

.submission-title {
  font-weight: bold;
  font-size: 1.1em;
  flex: 1 1 100%;
}

.submission-language {
  color: #555;
  font-size: 0.9em;
}

.submission-date {
  color: #777;
  font-size: 0.85em;
}

.submission-code {
  color: #444;
  margin: 10px 0;
  line-height: 1.5;
}

.article-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.modal h3 {
  margin-top: 0;
}
</style>