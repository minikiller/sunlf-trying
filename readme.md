## 用于演示如何使用fabric8-maven-plugin 
### biz-service
create from karaf-bundle-archetype
### biz-feature
create from karaf-feature-archetype
### biz-assembly
create from karaf-assembly-archetype
## install kong
### create project 
```batch
oc new-project api-gateway
oc adm policy add-scc-to-user anyuid -z default
```
### create postgresql 
```batch
oc new-app sameersbn/postgresql -e 'DB_PASS=kong' -e 'DB_NAME=kong' \
-e 'DB_USER=kong' --name kong-database
```
### create db-migration
```batch
apiVersion: batch/v1
kind: Job
metadata:
  name: kong-migration
spec:
  template:
    metadata:
      name: kong-migration
    spec:
      containers:
      - name: kong-migration
        image: kong
        env:
          - name: KONG_NGINX_DAEMON
            value: 'off'
          - name: KONG_PG_USER
            value: kong
          - name: KONG_PG_PASSWORD
            value: "kong"
          - name: KONG_PG_DATABASE
            value: 'kong'
          - name: KONG_PG_HOST
            value: lkong-databases.svc.cluster.local
        command: [ "/bin/sh", "-c", "kong migrations up" ]
      restartPolicy: Never
```
```batch
oc create -f kong_migration_postgres.yaml
```
###  create kong
```batch
oc new-app kong -e 'KONG_PG_DATABASE=kong' -e 'KONG_PG_USER=kong' -e 'KONG_PG_PASSWORD=kong' \
-e 'KONG_PG_HOST=kong-database' KONG_PROXY_ACCESS_LOG=/dev/stdout KONG_PROXY_ERROR_LOG=/dev/stderr \
-e 'KONG_ADMIN_LISTEN=0.0.0.0:8001'
```

### create kong-dashboard
```batch
oc new-app pgbi/kong-dashboard -e "kong-url='http://kong:8001'" 
```
修改 kong-dashboard参数
```batch
       - command:
            - npm
            - start
            - '--'
            - '--kong-url=http://kong:8001'
          image: >-
            pgbi/kong-dashboard@sha256:7c9a4e670718480f9e082b7ace0895deadae29dd831d2a1ce7aecd0f8eb200ed
          imagePullPolicy: Always
          name: kong-dashboard
```

### create konga
```batch
 oc new-app pantsel/konga 
 
 Admin
 login: admin | password: adminadminadmin
 
 Demo user
 login: demo | password: demodemodemo
```