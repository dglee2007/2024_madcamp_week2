# Concert Planner

## 소개
Concert Planner는 공연에 관심 있는 사람들끼리 정보를 공유하며 공연을 계획할 수 있게 해주는 앱입니다.

<div style="display: flex; justify-content: center;">
  <img src="https://github.com/dglee2007/2024_madcamp_week2/assets/83354877/31189fdb-317d-41e7-803a-68b086c611b6" style="width: 50%;">
</div>



## Tech Stack

**FE**

-   언어: Kotlin
-   IDE: Android Studio

**BE**

-   클라우드: AWS EC2
-   서버: Node.js
-   DB: MySQL RDS

  ![erdff](https://github.com/dglee2007/2024_madcamp_week2/assets/83354877/ea732ae7-8bcd-43d7-8fab-404de4ad0b49)




----------

## Detail

### 1. 로그인 화면

`Splash` 화면 이후 카카오 로그인 화면이 나옵니다.

로그인은 `Kakao API`를 사용하였습니다.

https://github.com/dglee2007/2024_madcamp_week2/assets/83354877/c37c4eb5-c1d1-4065-a24b-5dc6c4cdd94b


### 2. 홈 화면

인터파크티켓에서 데이터를 크롤링하여 수집했고 ViewPager를 이용해서 콘서트 목록이 자동스크롤되도록 하였고 더보기 버튼을 누르면 `RecyclerView` 로 모두 볼 수 있습니다. 공연을 등록하면 마이페이지에서 확인할 수 있습니다.

https://github.com/dglee2007/2024_madcamp_week2/assets/83354877/3ec1de5e-ad75-4810-a8bc-d5566786f1b9

### 3. 채팅

사용자는 채팅방을 새로 만들 수 있고, 웹소켓을 이용하여 node.js 서버와 안드로이드 간에 채팅을 보내고 받을 수 있습니다. 검색 기능을 이용해서 원하는 주제의 채팅방에 참여할 수 있고 채팅방을 만들 수 있습니다.

https://github.com/dglee2007/2024_madcamp_week2/assets/83354877/85eaedea-1b79-4c90-884f-dfa8b4ebad3e

### 4. 리뷰

공연 후기를 작성할 수 있습니다. 공연 후기에는 제목, 글, 사진을 올릴 수 있습니다. 사진은 갤러리 또는 카메라와 연동하여 첨부할 수 있습니다. 공연후기도 검색 기능을 이용해서 원하는 공연의 후기를 볼 수 있고 자신만의 공연 후기도 남길 수 있습니다.

https://github.com/dglee2007/2024_madcamp_week2/assets/83354877/2092a0a5-ed87-4f3b-b54a-3e1d5c4b2a62

### 5. 마이페이지

일정을 관리 및 로그아웃을 할 수 있습니다. 일정은 `CalenderView`를 이용하였고 등록한 공연을 확인할 수 있습니다.

https://github.com/dglee2007/2024_madcamp_week2/assets/83354877/3208fc48-57d2-4388-99c8-b1b1c2775be7

----------

### Team

이은재
https://github.com/lucy1287

이동건
https://github.com/dglee2007

### Download

https://drive.google.com/file/d/1Uyc4EAjZWXpWPktYMvtyO0ePDg77D0fO/view?usp=sharing
