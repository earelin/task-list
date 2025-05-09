terraform {
  required_version = ">= 1.11.0"
}

resource "kubernetes_namespace_v1" "task-list" {
  metadata {
    name = var.namespace
  }
}

module "mongodb" {
  source    = "./modules/mongodb"
  namespace = var.namespace
}

module "app" {
  source    = "./modules/app"
  namespace = var.namespace
}

resource "kubernetes_storage_class_v1" "local-storage-retain" {
  metadata {
    name = "local-storage-retain-class"
  }
  reclaim_policy = "Retain"
  storage_provisioner = "k8s.io/minikube-hostpath"
}
