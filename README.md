# mit_map_viewer project
(지도 데이터를 parsing하여 web 상에 표현 및 운영할 수 있도록 하는 viewer 프로젝트 입니다.)

## Environments
* Back-End
  * Amazon Corretto Version 17.0.7
  * Spring Boot 3.2.0
  * Gradle
  * lombok
  * webflux
* Front-End
  * html
  * java script

## 프로젝트 패키지 구성
* src.main.java.com.tmap.mit.map_viewer
    * cd (file 내에서 정의 된 code 값을 정의하고 검증 로직을 분리하기 위해서 생성)
    * config (todo - cache 처리 등 어플리케이션 고도화에 필요한 config 추가 예정)
    * constant (자주 사용되는 상수 설정 - 현재 shape file parsing 작업 중이라 이해하기 쉽도록 file 구조 기준으로 나누어 상수 정의)
    * controller (view를 return하거나 api 형태로 지도 데이터 추출 데이터를 전달하는 기능 정의)
    * dto (view로 넘겨주는 지도 데이터 중 유의미한 데이터 기준으로 dto 정의)
    * error (todo - 향후에 공통화된 error 처리 추가 예정)
    * service (실제로 지도 데이터를 추출하는 기능)
* src.main.resource
    * files (shp/dbf/shx 파일이 존재하는 패키지)
    * templates (view html)
* src.test.java.com.tmap.mit.map_viewer
    * controller (controller test code - BBD 형태)

