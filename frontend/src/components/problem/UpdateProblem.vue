<script setup lang="ts">
import TagPicker from "@/components/tag/TagPicker.vue";
import {client} from "@/client/client";
import {ProblemDto, TagsDto} from "@/client/data";

const props = defineProps<{
  problem: ProblemDto;
}>();


const addSample = () => {
  props.problem.samples.push({
    id: null,
    input: '',
    output: ''
  });
};

const removeSample = (index: number) => {
  if (props.problem.samples.length > 1) {
    props.problem.samples.splice(index, 1);
  } else {
    alert('You need at least one sample case');
  }
};

const addTest = () => {
  props.problem.tests.push({
    id: null,
    input: '',
    output: ''
  });
};

const removeTest = (index: number) => {
  if (props.problem.tests.length > 1) {
    props.problem.tests.splice(index, 1);
  } else {
    alert('You need at least one test case');
  }
};

const submitProblem = () => {
  client.problems.update(props.problem.id, {
    title: props.problem.title,
    description: props.problem.description,
    input: props.problem.input,
    output: props.problem.output,
    memoryLimitBytes: props.problem.memoryLimitBytes,
    timeLimitMilliseconds: props.problem.timeLimitMilliseconds,
    samples: props.problem.samples,
    tests: props.problem.tests,
    tags: props.problem.tags
  });
};

const handleTagsUpdate = (params: TagsDto) => {
  props.problem.tags = params.tags.map(tag => tag.id);
};
</script>

<template>
  <div class="problem-creator">
    <h2>Edit Problem</h2>

    <form @submit.prevent="submitProblem">
      <div class="form-section">
        <h3>Basic Information</h3>
        <div class="form-group">
          <label for="title">Problem Title</label>
          <input
              id="title"
              v-model="props.problem.title"
              type="text"
              required
              placeholder="Enter problem title"
          >
        </div>
      </div>

      <div class="form-section">
        <h3>Problem Description</h3>
        <div class="form-group">
          <label for="description">Description (Markdown supported)</label>
          <textarea
              id="description"
              v-model="props.problem.description"
              rows="8"
              required
              placeholder="Describe the problem in detail..."
          ></textarea>
        </div>
      </div>

      <div class="form-section">
        <h3>Input/Output Format</h3>
        <div class="form-group">
          <label for="inputFormat">Input Format</label>
          <textarea
              id="inputFormat"
              v-model="props.problem.input"
              rows="4"
              placeholder="Describe the input format..."
          ></textarea>
        </div>

        <div class="form-group">
          <label for="outputFormat">Output Format</label>
          <textarea
              id="outputFormat"
              v-model="props.problem.output"
              rows="4"
              placeholder="Describe the output format..."
          ></textarea>
        </div>
      </div>

      <div class="form-section">
        <h3>Sample Test Cases</h3>
        <div v-for="(sample, index) in problem.samples" :key="index" class="sample-case">
          <div class="sample-header">
            <h4>Sample {{ index + 1 }}</h4>
            <button type="button" @click="removeSample(index)" class="btn-remove">Remove</button>
          </div>
          <div class="form-group">
            <label :for="'sampleInput'+index">Input</label>
            <textarea
                :id="'sampleInput'+index"
                v-model="sample.input"
                rows="3"
                placeholder="Sample input..."
            ></textarea>
          </div>
          <div class="form-group">
            <label :for="'sampleOutput'+index">Output</label>
            <textarea
                :id="'sampleOutput'+index"
                v-model="sample.output"
                rows="3"
                placeholder="Sample output..."
            ></textarea>
          </div>
        </div>
        <button type="button" @click="addSample" class="btn-add">Add Sample</button>
      </div>

      <div class="form-section">
        <h3>Test Cases</h3>
        <div class="form-group">
          <label>Time Limit (milliseconds)</label>
          <input
              type="number"
              v-model.number="props.problem.timeLimitMilliseconds"
              min="100"
              step="100"
              required
          >
        </div>

        <div class="form-group">
          <label>Memory Limit (Bytes)</label>
          <input
              type="number"
              v-model.number="props.problem.memoryLimitBytes"
              min="1"
              step="1"
              required
          >
        </div>

        <div v-for="(test, index) in problem.tests" :key="index" class="test-case">
          <div class="test-header">
            <h4>Test Case {{ index + 1 }}</h4>
            <button type="button" @click="removeTest(index)" class="btn-remove">Remove</button>
          </div>
          <div class="form-group">
            <label :for="'testInput'+index">Input</label>
            <textarea
                :id="'testInput'+index"
                v-model="test.input"
                rows="3"
                placeholder="Test input..."
                required
            ></textarea>
          </div>
          <div class="form-group">
            <label :for="'testOutput'+index">Output</label>
            <textarea
                :id="'testOutput'+index"
                v-model="test.output"
                rows="3"
                placeholder="Test output..."
                required
            ></textarea>
          </div>
        </div>
        <button type="button" @click="addTest" class="btn-add">Add Test Case</button>
      </div>

      <div class="form-section">
        <h3>Tags</h3>
        <div class="form-group">
          <TagPicker :tags="client.tags.cache" :selected-tags="props.problem.tags.map(id => client.tags.getById(id))"
                     @tags-update="handleTagsUpdate"/>
        </div>
      </div>

      <div class="form-actions">
        <button type="submit" class="btn-submit">Save Problem</button>
      </div>
    </form>
  </div>
</template>

<style scoped>
.problem-creator {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.form-section {
  margin-bottom: 30px;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 6px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: 600;
  color: #333;
}

input[type="text"],
input[type="number"],
select,
textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

textarea {
  min-height: 80px;
  resize: vertical;
  font-family: monospace;
}

.sample-case,
.test-case {
  margin-bottom: 20px;
  padding: 15px;
  background: #fff;
  border: 1px solid #eee;
  border-radius: 4px;
}

.sample-header,
.test-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.btn-add,
.btn-remove,
.btn-submit,
.btn-cancel {
  padding: 8px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-add {
  background: #4CAF50;
  color: white;
}

.btn-remove {
  background: #f44336;
  color: white;
}

.btn-submit {
  background: #2196F3;
  color: white;
  margin-left: 10px;
}

.btn-cancel {
  background: #9E9E9E;
  color: white;
}

.btn-add:hover {
  background: #45a049;
}

.btn-remove:hover {
  background: #d32f2f;
}

.btn-submit:hover {
  background: #0b7dda;
}

.btn-cancel:hover {
  background: #757575;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

h2, h3, h4 {
  color: #2c3e50;
}

h2 {
  margin-bottom: 20px;
}

h3 {
  margin-top: 0;
  margin-bottom: 15px;
  padding-bottom: 5px;
  border-bottom: 1px solid #eee;
}

h4 {
  margin: 0;
}
</style>