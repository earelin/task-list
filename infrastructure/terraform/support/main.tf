terraform {
  required_version = ">= 1.11.0"
}

resource "kubernetes_namespace_v1" "common_namespace" {
  metadata {
    name = local.common_namespace_name
  }
}

module "jaeger" {
  source    = "./modules/jaeger"
  namespace = local.common_namespace_name
}

module "prometheus" {
  source    = "./modules/prometheus"
  namespace = local.common_namespace_name
}

resource "kubernetes_storage_class_v1" "local-storage-retain" {
  metadata {
    name = "local-storage-retain-class-${local.common_namespace_name}"
  }
  # reclaim_policy = "Retain"
  storage_provisioner = "k8s.io/minikube-hostpath"
}
