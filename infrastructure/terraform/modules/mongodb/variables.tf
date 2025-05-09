variable "namespace" {
  type    = string
}

variable "storage-class" {
  type    = string
  default = "local-storage-retain-class"
}
