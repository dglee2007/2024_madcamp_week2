# Concert Planner

## 소개
Concert Planner는 공연에 관심 있는 사람들끼리 정보를 공유하며 공연을 계획할 수 있게 해주는 앱입니다.

![My Image](https://drive.google.com/drive/u/0/folders/19SX3s_jMs1w1vbkJqOwx92IoSUgEbHo9)

## Tech Stack

**FE**

-   언어: Kotlin
-   IDE: Android Studio

**BE**

-   클라우드: AWS EC2
-   서버: Node.js
-   DB: MySQL RDS

![erdff.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/31f7313c-578f-4656-9901-019706622d94/erdff.png)

----------

## Detail

### 1. 로그인 화면

`Splash` 화면 이후 카카오 로그인 화면이 나옵니다.

로그인은 `Kakao API`를 사용하였습니다.

[Screen_Recording_20240710_195944_Concert Planner.mp4](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/9ba6e612-f111-473d-aa50-b70d493bae41/Screen_Recording_20240710_195944_Concert_Planner.mp4)

### 2. 홈 화면

인터파크티켓에서 데이터를 크롤링하여 수집했고 ViewPager를 이용해서 콘서트 목록이 자동스크롤되도록 하였고 더보기 버튼을 누르면 `RecyclerView` 로 모두 볼 수 있습니다. 공연을 등록하면 마이페이지에서 확인할 수 있습니다.

[Screen_Recording_20240710_201616_Concert Planner.mp4](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/ba37eff8-5ed7-4d6c-b68e-fd5ebcc478df/Screen_Recording_20240710_201616_Concert_Planner.mp4)

### 3. 채팅

사용자는 채팅방을 새로 만들 수 있고, 웹소켓을 이용하여 node.js 서버와 안드로이드 간에 채팅을 보내고 받을 수 있습니다. 검색 기능을 이용해서 원하는 주제의 채팅방에 참여할 수 있고 채팅방을 만들 수 있습니다.

[Screen_Recording_20240710_200232_Concert Planner.mp4](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/90889574-62ef-436a-bcdd-71591bc9a819/Screen_Recording_20240710_200232_Concert_Planner.mp4)

### 4. 리뷰

공연 후기를 작성할 수 있습니다. 공연 후기에는 제목, 글, 사진을 올릴 수 있습니다. 사진은 갤러리 또는 카메라와 연동하여 첨부할 수 있습니다. 공연후기도 검색 기능을 이용해서 원하는 공연의 후기를 볼 수 있고 자신만의 공연 후기도 남길 수 있습니다.

[Screen_Recording_20240710_200724_Concert Planner.mp4](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/0a5d174f-7c4f-4c7f-95e9-5cbc149530ed/Screen_Recording_20240710_200724_Concert_Planner.mp4)

### 5. 마이페이지

일정을 관리 및 로그아웃을 할 수 있습니다. 일정은 `CalenderView`를 이용하였고 등록한 공연을 확인할 수 있습니다.

[Screen_Recording_20240710_200750_Concert Planner.mp4](https://prod-files-secure.s3.us-west-2.amazonaws.com/f6cb388f-3934-47d6-9928-26d2e10eb0fc/473b8085-1550-410e-8f5d-a82757adb34b/Screen_Recording_20240710_200750_Concert_Planner.mp4)

----------

### Team

이은재

[lucy1287 - Overview](https://github.com/lucy1287)

직접 AWS에 서버 배포를 하고 RDS를 생성해서 연결하는 게 처음이었는데

이동건
https://github.com/dglee2007
