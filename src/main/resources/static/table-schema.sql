-- 불법 주정차 데이터 베이스
CREATE
    DATABASE illegal_parking;

-- 법정동코드
DROP TABLE law_dong;
CREATE TABLE law_dong
(
    DongSeq INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    Code    VARCHAR(10)  NOT NULL,              -- 법정동 코드
    Name    VARCHAR(100) NOT NULL,              -- 법정동 이름
    IsDel   BOOLEAN      NOT NULL DEFAULT FALSE -- 삭제 여부
) ENGINE = InnoDB
  CHARSET = utf8;

-- * code index 처리 필요 *
CREATE INDEX dongCode ON law_dong (Code);

-- 공영주차장 정보
DROP TABLE parking;
CREATE TABLE parking
(
    ParkingSeq           INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    PrkplceNo            VARCHAR(20),                       -- 주차장관리번호
    PrkplceNm            VARCHAR(30),                       -- 주차장명
    PrkplceSe            VARCHAR(10),                       -- 주차장구분
    PrkplceType          VARCHAR(10),                       -- 주차장유형
    Rdnmadr              VARCHAR(50),                       -- 소재지도로명주소
    Lnmadr               VARCHAR(50),                       -- 소재지지번주소
    Prkcmprt             INT,                               -- 주차구획수
    FeedingSe            INT,                               -- 급지구분
    EnforceSe            VARCHAR(5),                        -- 부제시행구분
    OperDay              VARCHAR(10),                       -- 운영요일
    WeekdayOperOpenHhmm  VARCHAR(5),                        -- 평일운영시작시각
    WeekdayOperColseHhmm VARCHAR(6),                        -- 평일운영종료시각
    SatOperOpenHhmm      VARCHAR(7),                        -- 토요일운영시작시각
    SatOperCloseHhmm     VARCHAR(8),                        -- 토요일운영종료시각
    HolidayOperOpenHhmm  VARCHAR(9),                        -- 공휴일운영시작시각
    HolidayOperCloseHhmm VARCHAR(10),                       -- 공휴일운영종료시각
    ParkingchrgeInfo     VARCHAR(2),                        -- 요금정보
    BasicTime            VARCHAR(5),                        -- 주차기본시간
    BasicCharge          INT,                               -- 주차기본요금
    AddUnitTime          VARCHAR(5),                        -- 추가단위시간
    AddUnitCharge        INT,                               -- 추가단위요금
    DayCmmtktAdjTime     VARCHAR(5),                        -- 1일주차권요금적용시간
    DayCmmtkt            INT,                               -- 1일주차권요금
    MonthCmmtkt          INT,                               -- 월정기권요금
    Metpay               VARCHAR(10),                       -- 결제방법
    Spcmnt               VARCHAR(50),                       -- 특기사항
    InstitutionNm        VARCHAR(20),                       -- 관리기관명
    PhoneNumber          VARCHAR(13),                       -- 전화번호
    Latitude             DECIMAL(18, 10),                   -- 위도
    Longitude            DECIMAL(18, 10),                   -- 경도
    ReferenceDate        DATE,                              -- 데이터기준일자
    Code                 VARCHAR(10) NOT NULL,              -- 법정동 코드
    IsDel                BOOLEAN     NOT NULL DEFAULT FALSE -- 삭제 여부
) ENGINE = InnoDB
  CHARSET = utf8;

-- 불법 주정차 구역
DROP TABLE illegal_zone;
CREATE TABLE illegal_zone
(
    ZoneSeq  BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    Polygon  POLYGON                           NOT NULL,              -- 불법 구역
    Code     VARCHAR(10)                       NOT NULL,              -- 법정동 코드
    EventSeq INT                               NULL,                  -- 불법주정차 구역 이벤트
    IsDel    BOOLEAN                           NOT NULL DEFAULT FALSE -- 삭제 여부
) ENGINE = InnoDB
  CHARSET = utf8;

-- 불법주정차 구역 이벤트
DROP TABLE illegal_event;
CREATE TABLE illegal_event
(
    EventSeq        INT AUTO_INCREMENT PRIMARY KEY,
    Name            VARCHAR(30) NULL,     -- 불법 구역 이름
    FirstStartTime  VARCHAR(5)  NULL,     -- 1차 이벤트 시작 시간
    FirstEndTime    VARCHAR(5)  NULL,     -- 1차 이벤트 종료 시간
    UsedFirst       BOOLEAN     NOT NULL, -- 1차 사용 여부
    SecondStartTime VARCHAR(5)  NULL,     -- 2차 이벤트 시작 시간
    SecondEndTime   VARCHAR(5)  NULL,     -- 2차 이벤트 종료 시간
    UsedSecond      BOOLEAN     NOT NULL, -- 2차 사용 여부
    IllegalType     VARCHAR(20) NOT NULL, -- 불법 구역 타입
    ZoneGroupType   VARCHAR(10) NOT NULL
) ENGINE = InnoDB
  CHARSET = utf8;

-- 차량 번호 정보 ( 신고 등록 및 접수 알림 차량 정보 )
DROP TABLE car;
CREATE TABLE car
(
    CarSeq       INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    CarNum       VARCHAR(10)                    NULL,                   -- 차 번호
    Name         VARCHAR(10)                    NULL,                   -- 차 이름
    Displacement VARCHAR(10)                    NULL,                   -- 배기량 ( 몇 CC )
    IsAlarm      BOOLEAN                        NOT NULL DEFAULT FALSE, -- 알림 받기 여부
    UserSeq      BIGINT                         NOT NULL,               -- 사용자 키
    IsDel        BOOLEAN                        NOT NULL DEFAULT FALSE, -- 삭제 여부
    DelDt        Datetime                       NULl                    -- 삭제 일자
) ENGINE = InnoDB
  CHARSET = utf8;

-- 신고 접수
DROP TABLE report;
CREATE TABLE report
(
    ReportSeq        INT AUTO_INCREMENT PRIMARY KEY,
    FirstReceiptSeq  INT          NOT NULL,               -- 1차 신고 등록
    SecondReceiptSeq INT          NOT NULL,               -- 2차 신고 등로
    ReportUserSeq    INT          NULL,                   -- 사용자 ( 기관 사용자 )
    RegDt            Datetime     NOT NULL,               -- 신고 등록 일자
    ResultType       VARCHAR(10)  NOT NULL DEFAULT 1,     -- 신고 접수 등록 여부 ( 정부 기관 사람 - 대기(1) / 신고제외(2) / 과태료대상(3)  )
    Note             VARCHAR(100) NULL,                   -- 비고
    ZoneSeq          INT          NOT NULL,               -- 불법 구역  ( 통계 : 불법 구역에 신고 건수 찾기 위한 용도 )
    Code             VARCHAR(10)  NOT NULL,               -- 법정동 코드 ( 통계 : 읍/면/동 기준으로 사고 건수 찾기 위한 용도 )
    IsDel            BOOLEAN      NOT NULL DEFAULT FALSE, -- 삭제 여부
    DelDt            Datetime     NULL                    -- 삭제 일자
) ENGINE = InnoDB
  CHARSET = utf8;

-- 신고 등록
DROP TABLE receipt;
CREATE TABLE receipt
(
    ReceiptSeq  INT AUTO_INCREMENT PRIMARY KEY,
    ZoneSeq     INT          NOT NULL,               -- 불법 구역
    RegDt       Datetime     NOT NULL,               -- 신고 등록 일자
    UserSeq     INT          NOT NULL,               -- 사용자 ( 일반 사용자 )
    CarNum      VARCHAR(10)  NULL,                   -- 차량 번호
    FileName    VARCHAR(50)  NOT NULL,               -- 파일 이름
    Note        VARCHAR(100) NOT NULL,               -- 사유 또는 비고
    Code        VARCHAR(10)  NOT NULL,               -- 법정동 코드
    ReceiptType VARCHAR(10)  NOT NULL,               -- 현재 상태 ( 신고 발생(1), 신고 접수(2), 신고 누락(3), 신고 제외(4), 과태료 대상(5) )
    Addr        VARCHAR(50)  NOT NULL,               -- 신고 등록 지역 주소 (지번주소)
    IsDel       BOOLEAN      NOT NULL DEFAULT FALSE, -- 삭제 여부
    DelDt       Datetime     NULL                    -- 삭제 일자
) ENGINE = InnoDB
  CHARSET = utf8;

-- 결제 정보
DROP TABLE calculate;
CREATE TABLE calculate
(
    CalculateSeq      INT AUTO_INCREMENT PRIMARY KEY,
    PointSeq          INT      NOT NULL,               -- 포인트 키
    UserSeq           INT      NOT NULL,               -- 사용자 키 ( 포인트 추가 or 포인트 사용자 )
    CurrentPointValue BIGINT   NOT NULL,               -- 현재 포인트 점수
    BeforePointValue  BIGINT   NOT NULL,               -- 이전 포인트 점수
    IsDel             BOOLEAN  NOT NULL DEFAULT FALSE, -- 삭제 여부
    RegDt             Datetime NOT NULL                -- 등록 일자
) ENGINE = InnoDB
  CHARSET = utf8;

-- 포인트
DROP TABLE point;
CREATE TABLE point
(
    PointSeq   INT AUTO_INCREMENT PRIMARY KEY,
    Note       VARCHAR(100) NULL,     -- 등록자
    Value      BIGINT       NOT NULL, -- 포인트 점수
    UserSeq    INT          NOT NULL, -- 등록자
    ReportSeq  INT          NULL,     -- 신고 키
    ProductSeq INT          NULL,     -- 제품 키
    PointType  VARCHAR(10)  NOT NULL  -- 상태 (추가 포이트(Plug) / 사용 포인트(Minus) )
) ENGINE = InnoDB
  CHARSET = utf8;

-- 상품
DROP TABLE product;
CREATE TABLE product
(
    ProductSeq INT AUTO_INCREMENT PRIMARY KEY,
    Name       VARCHAR(30) NOT NULL,               -- 상품 이름
    Brand      VARCHAR(20) NOT NULL,               -- 브랜드 이름
    PointValue BIGINT      NOT NULL,               -- 포인트 점수
    UserSeq    INT         NOT NULL,               -- 등록자
    IsDel      BOOLEAN     NOT NULL DEFAULT FALSE, -- 삭제 여부
    RegDt      Datetime    NOT NULL                -- 등록 일자
) ENGINE = InnoDB
  CHARSET = utf8;

-- 회원 정보 ( 신고자 및 신고 알림 수신자 및 기관 )
DROP TABLE user;
CREATE TABLE user
(
    UserSeq  BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    Name     VARCHAR(10)                       NOT NULL,               -- 회원 이름
    Email    VARCHAR(20)                       NOT NULL,               -- email (id)
    Password VARCHAR(50)                       NOT NULL,               -- 패스워드
    UserCode BIGINT                            NOT NULL,               -- 사용자 고유 체번 ( 예> 기관 사람 / 일반 사용자 구분 )
    Role     INT                               NOT NULL,               -- 역할 ( USER / ADMIN / GOVERNMENT )
    IsDel    BOOLEAN                           NOT NULL DEFAULT FALSE, -- 삭제 여부
    DelDt    Datetime                          NULL                    -- 삭제 일자
) ENGINE = InnoDB
  CHARSET = utf8;

-- 환경 설정 (보류)
DROP TABLE environment;
CREATE TABLE environment
(
    EnvironmentSeq INT AUTO_INCREMENT PRIMARY KEY,
    ZoneGroupType  VARCHAR(10) NOT NULL, -- 그룹 이름
    RegDt          Datetime    NOT NULL  -- 등록 일자
) ENGINE = InnoDB
  CHARSET = utf8;


-- ---------------------------------------------------------------------------------------------------
