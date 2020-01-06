FROM nginx:alpine

COPY nginx.conf /nginx.conf
COPY entrypoint.sh /entrypoint.sh

WORKDIR /usr/share/nginx/html
COPY generator-frontend/ .
ENV API_BACKEND=127.0.0.1:8080

ENTRYPOINT ["/entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
