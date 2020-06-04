# Kubernetes Application Setup  

Before start of application storage paths(hostPath.path) in storage.yaml should be changed to host system path.  

If you are using Docker desktop for Windows you need to add the path to Resources -> File Sharing  
And let’s say you want to use path from Windows machine use path like below  

Windows Host Path: E:\kube\kubernetes\springboot  
Kubernetes Path: /host_mnt/E/kube/kubernetes/springboot/  

Resource limits for each container can be set using the below settings.  

You can set CPU limit in milli Cores, 1000m - 1 CPU Core  
Memory Limit - Mi for MB, Gi for GB  

       resources:  
          limits:  
            memory: 1Gi  
            cpu: 2000m  
          requests:  
            cpu: 600m  
            memory: 600Mi  

			
To start application execute below commands  

kubectl apply -f storage.yaml  
kubectl apply -f mysql.yaml  
kubectl apply -f springboot.yaml  
kubectl apply -f httpd.yaml  


To enable auto scalling please follow instructions from [metric-server](https://github.com/karthiksurabathula/school/tree/master/kubernetes/metrics-server)

Application is accessable from http://localhost:30082/ or http://**host ip address**:30082/
