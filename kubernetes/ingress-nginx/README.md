# Install Application

[To deploy application follow instructions from  standalone](https://github.com/karthiksurabathula/school/tree/master/kubernetes/standalone)

Once you are able to access application from node port and using standalone install, to access application ingress NodePort should be changed to ClusterIP to modify service. apply the below to modify service.

kubectl apply -f httpd-ClusterIp-service.yaml

# Install Nginx Ingress

Once Application is installed follow instructions below 

Install Helm packate manager on the your host.

helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx  
helm repo update  
helm install controller ingress-nginx/ingress-nginx  

kubectl apply -f ingress-tls-secret.yaml  
kubectl apply -f ingress.yaml
