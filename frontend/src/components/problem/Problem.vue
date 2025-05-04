<template>
  <div class="problem-page">
    <div class="problem-header">
      <h1>{{ problem.title }}</h1>
      <div class="problem-meta">
        <span class="time-limit">Time limit: {{ problem.timeLimit }}</span>
        <span class="memory-limit">Memory limit: {{ problem.memoryLimit }}</span>
        <span class="problem-id">Problem ID: {{ problem.id }}</span>
      </div>
    </div>

    <div class="problem-content">
      <div class="problem-statement">
        <div v-html="problem.statement"></div>

        <div class="input-specification">
          <h3>Input</h3>
          <div v-html="problem.inputSpec"></div>
        </div>

        <div class="output-specification">
          <h3>Output</h3>
          <div v-html="problem.outputSpec"></div>
        </div>

        <div class="sample-tests">
          <h3>Examples</h3>
          <div v-for="(sample, index) in problem.samples" :key="index" class="sample-test">
            <div class="input">
              <h4>Input #{{ index + 1 }}</h4>
              <pre>{{ sample.input }}</pre>
            </div>
            <div class="output">
              <h4>Output #{{ index + 1 }}</h4>
              <pre>{{ sample.output }}</pre>
            </div>
          </div>
        </div>

        <div class="note" v-if="problem.note">
          <h3>Note</h3>
          <div v-html="problem.note"></div>
        </div>
      </div>

      <div class="code-submission">
        <div class="language-selector">
          <label for="language">Language:</label>
          <select id="language" v-model="selectedLanguage">
            <option v-for="lang in languages" :value="lang.value" :key="lang.value">
              {{ lang.name }}
            </option>
          </select>
        </div>

        <div class="code-editor">
          <textarea
              v-model="userCode"
              placeholder="Enter your code here..."
              spellcheck="false"
          ></textarea>
        </div>

        <div class="custom-input">
          <h4>Custom Input (optional)</h4>
          <textarea
              v-model="customInput"
              placeholder="Enter custom input to test your code..."
              rows="5"
          ></textarea>
        </div>

        <div class="submission-controls">
          <button @click="runCode" class="run-button">Run</button>
          <button @click="submitCode" class="submit-button">Submit</button>
        </div>

        <div v-if="testResults" class="test-results">
          <h3>Test Results</h3>
          <div v-if="testResults.status === 'success'" class="result-success">
            <p>✓ All tests passed!</p>
            <p>Time: {{ testResults.time }} ms</p>
            <p>Memory: {{ testResults.memory }} KB</p>
          </div>
          <div v-else class="result-error">
            <p>✗ {{ testResults.message }}</p>
            <div v-if="testResults.details" class="error-details">
              <pre>{{ testResults.details }}</pre>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProblemPage',
  data() {
    return {
      problem: {
        id: '123A',
        title: 'Sum of Two Numbers',
        timeLimit: '1 second',
        memoryLimit: '256 MB',
        statement: `
          <p>You are given two integers <strong>a</strong> and <strong>b</strong>.
          Your task is to calculate and print their sum.</p>
        `,
        inputSpec: `
          <p>The input contains two integers <strong>a</strong> and <strong>b</strong>
          (−1000 ≤ a, b ≤ 1000), separated by a space.</p>
        `,
        outputSpec: `
          <p>Output a single integer — the sum of <strong>a</strong> and <strong>b</strong>.</p>
        `,
        samples: [
          {
            input: '5 8',
            output: '13'
          },
          {
            input: '-3 2',
            output: '-1'
          }
        ],
        note: `
          <p>This is a simple problem to test your submission and judging system.</p>
        `
      },
      languages: [
        { value: 'cpp', name: 'C++' },
        { value: 'java', name: 'Java' },
        { value: 'python', name: 'Python' },
        { value: 'javascript', name: 'JavaScript' }
      ],
      selectedLanguage: 'cpp',
      userCode: `#include <iostream>\nusing namespace std;\n\nint main() {\n    int a, b;\n    cin >> a >> b;\n    cout << a + b << endl;\n    return 0;\n}`,
      customInput: '',
      testResults: null
    }
  },
  methods: {
    runCode() {
      // In a real app, this would send the code to a backend for execution
      // This is a mock implementation for demonstration

      if (!this.userCode.trim()) {
        this.testResults = {
          status: 'error',
          message: 'Please write some code before running.'
        };
        return;
      }

      // Simulate API call delay
      this.testResults = null;

      setTimeout(() => {
        // Mock different test results
        const random = Math.random();

        if (random > 0.3) {
          this.testResults = {
            status: 'success',
            message: 'All tests passed!',
            time: Math.floor(Math.random() * 100) + 50,
            memory: Math.floor(Math.random() * 2000) + 1000
          };
        } else if (random > 0.15) {
          this.testResults = {
            status: 'error',
            message: 'Wrong answer on test 2',
            details: 'Expected output: -1\nYour output: 0'
          };
        } else {
          this.testResults = {
            status: 'error',
            message: 'Runtime error',
            details: 'Segmentation fault (core dumped)'
          };
        }
      }, 1000);
    },
    submitCode() {
      // In a real app, this would submit the code for official judging
      alert(`Code submitted in ${this.selectedLanguage}!`);
    }
  }
}
</script>

<style scoped>
.problem-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Helvetica Neue', Arial, sans-serif;
  color: #333;
}

.problem-header {
  margin-bottom: 30px;
  border-bottom: 1px solid #ddd;
  padding-bottom: 15px;
}

.problem-header h1 {
  margin: 0;
  color: #2c3e50;
}

.problem-meta {
  margin-top: 10px;
  font-size: 14px;
  color: #666;
}

.problem-meta span {
  margin-right: 20px;
}

.problem-content {
  display: flex;
  gap: 30px;
}

.problem-statement {
  flex: 2;
  line-height: 1.6;
}

.problem-statement h2, .problem-statement h3, .problem-statement h4 {
  color: #2c3e50;
  margin-top: 25px;
}

.problem-statement pre {
  background: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  overflow-x: auto;
}

.sample-test {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.sample-test .input, .sample-test .output {
  flex: 1;
}

.sample-test pre {
  margin: 5px 0 0 0;
}

.code-submission {
  flex: 1;
  min-width: 400px;
}

.language-selector {
  margin-bottom: 15px;
}

.language-selector select {
  padding: 5px;
  margin-left: 10px;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.code-editor textarea {
  width: 100%;
  height: 300px;
  font-family: 'Consolas', 'Monaco', monospace;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
  margin-bottom: 15px;
}

.custom-input textarea {
  width: 100%;
  font-family: 'Consolas', 'Monaco', monospace;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
  margin-bottom: 15px;
}

.submission-controls {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.submission-controls button {
  padding: 8px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}

.run-button {
  background-color: #3498db;
  color: white;
}

.run-button:hover {
  background-color: #2980b9;
}

.submit-button {
  background-color: #2ecc71;
  color: white;
}

.submit-button:hover {
  background-color: #27ae60;
}

.test-results {
  padding: 15px;
  border-radius: 4px;
  margin-top: 20px;
}

.result-success {
  background-color: #dff0d8;
  color: #3c763d;
}

.result-error {
  background-color: #f2dede;
  color: #a94442;
}

.error-details pre {
  margin: 10px 0 0 0;
  white-space: pre-wrap;
}
</style>