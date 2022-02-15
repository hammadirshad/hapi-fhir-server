{{/*chart name and version*/}}
{{- define "fhir.server.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}


{{/*common labels*/}}
{{- define "fhir.server.labels" -}}
helm.sh/chart: {{ include "fhir.server.chart" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*app selector labels*/}}
{{- define "server.selectorLabels" -}}
app.kubernetes.io/name: "{{ .Values.server.name }}"
app.kubernetes.io/instance: {{ .Release.Name | quote }}
{{- end }}

{{/*spring env*/}}
{{- define "spring.application.env" -}}
- name: SPRING_PROFILES_ACTIVE
  value: kubernetes
{{- end }}

{{/*volume Mount*/}}

{{/*log mounts*/}}
{{- define "logs.volumeMount" -}}
- name:  {{.Values.storage.log.name }}
  mountPath: /log
{{- end }}

{{- define "logs.volume" -}}
- name:  {{.Values.storage.log.name }}
{{- if .Values.storage.log.link }}
  persistentVolumeClaim:
    claimName: {{.Values.storage.log.name }}
{{- else }}
  emptyDir: { }
{{- end }}
{{- end }}
