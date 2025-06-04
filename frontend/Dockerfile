FROM node:18-alpine AS builder
LABEL authors="Ray_1024"

WORKDIR /app
COPY package*.json ./
RUN npm install
COPY ./ .
RUN npm run build

FROM nginx:alpine
LABEL authors="Ray_1024"
COPY --from=builder /app/dist /usr/share/nginx/html
#COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]