#!/bin/sh

export API_BACKEND

envsubst '${API_BACKEND}' < /nginx.conf > /etc/nginx/nginx.conf

exec "$@"
