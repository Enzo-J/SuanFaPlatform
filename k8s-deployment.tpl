apiVersion: apps/v1
kind: Deployment
metadata:
  name: {APP_NAME}-deployment
  labels:
    app: {APP_NAME}
spec:
  replicas: 1
  selector:
    matchLabels:
      app: {APP_NAME}
  template:
    metadata:
      labels:
        app: {APP_NAME}
    spec:
      imagePullSecrets:
      - name: login
      containers:
      - name: {APP_NAME}
        image: {IMAGE_URL}:{IMAGE_TAG}
        ports:
        - containerPort: 8080
        volumeMounts:
          - name: "upload-file"
            mountPath: "/opt/upload"
            readOnly: false
      volumes:
      - name: "upload-file"
        hostPath:
          path: "/u01/isi/k8s/app/upload"



#---

# ------------------- Service ------------------- #

#kind: Service
#apiVersion: v1
#metadata:
#  labels:
#    app: {APP_NAME}
#  name: {APP_NAME}-service
#spec:
#  type: NodePort
#  ports:
#    - port: 443
#      targetPort: 8080
#      nodePort: 31694
#  selector:
#    app: {APP_NAME}