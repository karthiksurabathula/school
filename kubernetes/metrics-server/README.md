# Enable Autoscalling  

You can configure number of replicas for Apache and Java application using below attributes from **hpav-2b1.yaml**  

minReplicas: 1
maxReplicas: 5

Number of instances of container increases if the resource  under **metrics:** are breached until it reaches the maximum replicas.  

To setup metric-server download latest files from https://github.com/kubernetes-sigs/metrics-server/tree/master/manifests/base  

Once you have latest files you can run the below command to start metrics-server  

kubectl apply -f .  

Get Usage of pods :  
kubectl top pod  

Get Usage of Nodes :  
kubectl top nodes  

To Check the running pods:  
kubectl get all -n kube-system   

