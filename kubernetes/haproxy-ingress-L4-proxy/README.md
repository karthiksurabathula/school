# Install Application  

[To deploy application follow instructions from  standalone](https://github.com/karthiksurabathula/school/tree/master/kubernetes/standalone)  

Once you are able to access application from node port and using standalone install, to access application ingress NodePort should be changed to ClusterIP to modify service. apply the below to modify service.  

kubectl apply -f service/.  

# Install HAProxy Ingress controller using kubectl  

kubectl apply -f haproxy-ingress-controller.yaml  

Once the haproxy-ingress controller is up and running execute the below.  

kubectl apply -f ingress-school.yaml  

If you have setup Prometheus  apply the below  

kubectl apply -f ingress-monitoring.yaml  
