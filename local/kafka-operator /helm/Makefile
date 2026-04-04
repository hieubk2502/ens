add-repo:
	helm repo add strimzi https://strimzi.io/charts/

update-repo:
	helm repo update

install-repo:
	helm install strimzi strimzi/strimzi-kafka-operator \
  		--namespace kafka

update-repo:
	helm repo update

check-pod:
	kubectl get pods -n kafka
