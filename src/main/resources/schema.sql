DROP TABLE IF EXISTS marques CASCADE;
DROP TABLE IF EXISTS voitures CASCADE;
DROP TYPE IF EXISTS type_carburant CASCADE;
DROP TYPE IF EXISTS transmission CASCADE;
DROP TYPE IF EXISTS type_vendeur CASCADE;

-- Création de la table Marques
CREATE TABLE marques (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL UNIQUE,
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Création des types pour les voitures
CREATE TYPE type_carburant AS ENUM (
    'ESSENCE',
    'DIESEL',
    'HYBRIDE',
    'ELECTRIQUE',
    'HYBRIDE_RECHARGEABLE'
);

CREATE TYPE transmission AS ENUM (
    'AUTOMATIQUE',
    'MANUELLE'
);

CREATE TYPE type_vendeur AS ENUM (
    'PARTICULIER',
    'CONCESSIONNAIRE'
);

-- Création de la table Voitures
CREATE TABLE voitures (
    id SERIAL PRIMARY KEY,
    id_marque INT NOT NULL,
    modele VARCHAR(80) NOT NULL,
    annee INT NOT NULL,
    kilometrage INT NOT NULL,
    prix INT NOT NULL,
    carburant type_carburant NOT NULL,
    transmission transmission NOT NULL,
    couleur VARCHAR(80),
    ville VARCHAR(100) NOT NULL,
    type_vendeur type_vendeur NOT NULL,
    date_publication DATE DEFAULT CURRENT_DATE,
    description TEXT,
    CONSTRAINT fk_id_marque FOREIGN KEY (id_marque) REFERENCES marques(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT ck_annee_valide CHECK ( annee >= 1900 ),
    CONSTRAINT ck_kilometrage_positif CHECK ( kilometrage >= 0 ),
    CONSTRAINT ck_prix_positif CHECK ( prix > 0 )
);

-- Des index pour faciliter le filtrage
CREATE INDEX idx_voitures_marque ON voitures(id_marque);
CREATE INDEX idx_voitures_prix ON voitures(prix);


