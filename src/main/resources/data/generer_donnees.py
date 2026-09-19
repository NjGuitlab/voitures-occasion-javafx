import csv

#Remplace les apostrophes simples pour la compatibilité SQL
def echapper_sql(valeur: str) -> str:
    return valeur.replace("'", "''")

def convertir_csv_vers_sql(csv_path: str, sql_path: str):
    lignes = []
    marques_uniques = []

# Lecture du fichier CSV
    with open(csv_path, mode="r", encoding="UTF-8") as f:
        lecteur = csv.reader(f)

        next(lecteur, None)
        
        for ligne in lecteur:
            if not ligne or len(ligne) < 13:
                continue
            lignes.append(ligne)

            nom_marque = ligne[1].strip()  # On récupère tous les noms de marque uniques
            if nom_marque not in marques_uniques:
                marques_uniques.append(nom_marque)

    # Un dictionnaire pour attribuer l'index d'une marque dans la liste à un ID
    marques_vers_id = {nom: idx + 1 for idx, nom in enumerate(marques_uniques)}

    # Génération du SQL
    with open(sql_path, mode="w", encoding="UTF-8") as out:
        out.write("-- Script généré automatiquement pour le peuplement de la base\n")
        out.write("BEGIN;\n\n") # Début de la transaction SQL

        # Insertions dans la table Marques
        out.write("-- 1. Insertion des marques\n")
        out.write("INSERT INTO marques (id, nom) VALUES\n")
        marques_values = [
            f"  ({marques_vers_id[m]}, '{echapper_sql(m)}')"
            for m in marques_uniques
        ]
        out.write(",\n".join(marques_values) + ";\n\n")

        # Réajustement de la séquence pour marques.id
        out.write("-- Synchonisation de la séquence auto-incrémentée de marques\n")
        out.write("SELECT setval(pg_get_serial_sequence('marques', 'id'), coalesce(max(id), 1)) FROM marques;\n\n")

        # Insertion dans la table voitures
        out.write("-- 2. Insertion des voitures\n")
        out.write(
        "INSERT INTO voitures ("
        "id, id_marque, modele, annee, kilometrage, prix, "
        "carburant, transmission, couleur, ville, type_vendeur, "
        "date_publication, description"
        ") VALUES\n"
        )

        voitures_values = []
        for ligne in lignes:
            id_voiture = int(ligne[0].strip())
            id_marque = marques_vers_id[ligne[1].strip()]
            modele = echapper_sql(ligne[2].strip())
            annee = int(ligne[3].strip())
            kilometrage = int(ligne[4].strip())
            prix = int(ligne[5].strip())
            carburant = ligne[6].strip()
            transmission = ligne[7].strip()
            couleur = echapper_sql(ligne[8].strip())
            ville = echapper_sql(ligne[9].strip())
            type_vendeur = ligne[10].strip()
            date_publication = ligne[11].strip()
            description = echapper_sql(ligne[12].strip())

            valeur_str = (
                f" ({id_voiture}, {id_marque}, '{modele}', {annee}, {kilometrage}, {prix}, "
                f"'{carburant}', '{transmission}', '{couleur}', '{ville}', '{type_vendeur}', "
                f" '{date_publication}', '{description}')"
            )
            voitures_values.append(valeur_str)

        out.write(",\n".join(voitures_values) + ";\n\n")

        # Réajustement de la séquence pour voitures.id
        out.write("-- Synchonisation de la séquence auto-incrémentée de voitures\n")
        out.write("SELECT setval(pg_get_serial_sequence('voitures', 'id'), coalesce(max(id), 1)) FROM voitures;\n\n")

        out.write("COMMIT;\n") # Commit de la transaction SQL

    print(f"[OK] {len(marques_uniques)} marques et {len(lignes)} voitures exportées dans {sql_path}.")

if __name__== "__main__":
    convertir_csv_vers_sql("voitures.csv", "donnees.sql")
