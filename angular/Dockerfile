# docker build -t karthiksurabathula/httpd:latest .
FROM httpd:2.4.43-alpine
RUN apk --no-cache add curl
# Remove any files that may be in the public htdocs directory already.
RUN rm -r /usr/local/apache2/htdocs/*

# Copy all the files from the docker build context into the public htdocs of the apache container.
COPY ./site/ /usr/local/apache2/htdocs/
COPY ./httpd.conf /usr/local/apache2/conf/

RUN chmod -R 775 /usr/local/apache2/htdocs/*
