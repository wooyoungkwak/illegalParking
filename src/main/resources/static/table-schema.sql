-- 불법 주정차 데이터 베이스
CREATE
DATABASE illegal_parking;

-- 법정동코드
-- DROP TABLE law_dong;
CREATE TABLE law_dong
(
    Code  BIGINT       NOT NULL,              -- 법정동 코드
    Name  VARCHAR(100) NOT NULL,              -- 법정동 이름
    IsDel BOOLEAN      NOT NULL DEFAULT FALSE -- 삭제 여부
) ENGINE = InnoDB
  CHARSET = utf8;

-- 공영주차장 정보
-- DROP TABLE parking
CREATE TABLE parking
(
    ParkingSeq           INT     NOT NULL PRIMARY KEY AUTO_INCREMENT,
    PrkplceNo            VARCHAR(20),                    -- 주차장관리번호
    PrkplceNm            VARCHAR(30),                    -- 주차장명
    PrkplceSe            VARCHAR(10),                    -- 주차장구분
    PrkplceType          VARCHAR(10),                    -- 주차장유형
    Rdnmadr              VARCHAR(50),                    -- 소재지도로명주소
    Lnmadr               VARCHAR(50),                    -- 소재지지번주소
    Prkcmprt             INT,                            -- 주차구획수
    FeedingSe            INT,                            -- 급지구분
    EnforceSe            VARCHAR(5),                     -- 부제시행구분
    OperDay              VARCHAR(10),                    -- 운영요일
    WeekdayOperOpenHhmm  VARCHAR(5),                     -- 평일운영시작시각
    WeekdayOperColseHhmm VARCHAR(6),                     -- 평일운영종료시각
    SatOperOpenHhmm      VARCHAR(7),                     -- 토요일운영시작시각
    SatOperCloseHhmm     VARCHAR(8),                     -- 토요일운영종료시각
    HolidayOperOpenHhmm  VARCHAR(9),                     -- 공휴일운영시작시각
    HolidayOperCloseHhmm VARCHAR(10),                    -- 공휴일운영종료시각
    ParkingchrgeInfo     VARCHAR(2),                     -- 요금정보
    BasicTime            VARCHAR(5),                     -- 주차기본시간
    BasicCharge          INT,                            -- 주차기본요금
    AddUnitTime          VARCHAR(5),                     -- 추가단위시간
    AddUnitCharge        INT,                            -- 추가단위요금
    DayCmmtktAdjTime     VARCHAR(5),                     -- 1일주차권요금적용시간
    DayCmmtkt            INT,                            -- 1일주차권요금
    MonthCmmtkt          INT,                            -- 월정기권요금
    Metpay               VARCHAR(10),                    -- 결제방법
    Spcmnt               VARCHAR(50),                    -- 특기사항
    InstitutionNm        VARCHAR(20),                    -- 관리기관명
    PhoneNumber          VARCHAR(13),                    -- 전화번호
    Latitude             DECIMAL(18, 10),                -- 위도
    Longitude            DECIMAL(18, 10),                -- 경도
    ReferenceDate        DATE,                           -- 데이터기준일자
    IsDel                BOOLEAN NOT NULL DEFAULT FALSE, -- 삭제 여부
    Code                 BIGINT                          -- 법정동 코드 키
) ENGINE = InnoDB
  CHARSET = utf8;

-- 불법 주정차 구역
-- DROP TABLE illegal_zone;
CREATE TABLE illegal_zone
(
    ZoneSeq   BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    Name      VARCHAR(30) NULL,               -- 불법 구역 이름
    Polygon   POLYGON NOT NULL,               -- 불법 구역
    IsDel     BOOLEAN NOT NULL DEFAULT FALSE, -- 삭제 여부
    StartTime Datetime NULL,                  -- 시작 시간
    EndTime   Datetime NULL,                  -- 종료 시간
    Code      BIGINT  NOT NULL,               -- 법정동 코드 키
    TypeSeq   INT     NOT NULL                -- 타입 키
) ENGINE = InnoDB
  CHARSET = utf8;

-- 불법 주정차 구역
-- DROP TABLE illegal_type;
CREATE TABLE illegal_type
(
    TypeSeq INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    Name    VARCHAR(30) NOT NULL,              -- 불법 구역 타입 이름 ( 예> 불법주청자, 5분주차, 탄력주차, 샘플주차 )
    IsDel   BOOLEAN     NOT NULL DEFAULT FALSE -- 삭제 여부
) ENGINE = InnoDB
  CHARSET = utf8;

-- 회원 정보
-- DROP TABLE users;
CREATE TABLE users
(
    UserSeq BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    Name    VARCHAR(10) NOT NULL,              -- 회원 이름
    Email   POLYGON NULL,                      -- email (id)
    Passwd  INT,                               -- 패스워드
    Type    BIGINT,                            -- 사용자 고유 체번
    IsDel   BOOLEAN     NOT NULL DEFAULT FALSE -- 삭제 여부
) ENGINE = InnoDB
  CHARSET = utf8;

-- 차량 번호 정보
CREATE TABLE car
(
    CarSeq  INT AUTO_INCREMENT PRIMARY KEY,
    CarName VARCHAR(10) NULL,              -- 차 이름
    CarNum  VARCHAR(10) NULL,              -- 차 번호
    UserSeq BIGINT  NOT NULL,              -- 사용자 키
    IsDel   BOOLEAN NOT NULL DEFAULT FALSE -- 삭제 여부
) ENGINE = InnoDB
  CHARSET = utf8;

-- ---------------------------------------------------------------------------------------------------




















