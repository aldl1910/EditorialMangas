CREATE TABLE EDITORIAL (
    ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    WEB CHAR(20),
    NOMBRE VARCHAR(20) NOT NULL,
    CONSTRAINT ID_EDITORIAL_PK PRIMARY KEY (ID)
);

CREATE TABLE MANGA (
    ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY, -- Id autonumérico
    NOMBRE VARCHAR(60) NOT NULL,
    AUTOR VARCHAR(40) NOT NULL,
    ISBN VARCHAR(20) NOT NULL,
    EMAIL_EDITORIAL VARCHAR(30),
    EDITORIAL INTEGER,
    FECHA_PUBLICAION DATE,
    CAPITULOS SMALLINT,
    VOLUMEN SMALLINT,
    NUMPAGINAS SMALLINT,
    PRECIO DECIMAL(7,2),
    IDIOMA VARCHAR(20),
    RESTRICCION_EDAD BOOLEAN,
    ESTADO VARCHAR(1),
    LOGO VARCHAR(30),
    CONSTRAINT ID_MANGA_PK PRIMARY KEY (ID),
    CONSTRAINT PROV_MANGA_FK FOREIGN KEY (EDITORIAL) REFERENCES EDITORIAL (ID)
);