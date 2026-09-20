# LowkeyCore

Plugin + resourcepack voor LowkeySMP.

**Wat het doet**
- CREW-badge bij spelers uit `config.yml` (nu alleen R3ktify): in de chat, de tablist en boven het hoofd
- Join en leave als groen (+) en rood (-) icoontje met de naam
- Bij een dood: rode melding met LOWKEY-badge ("Naam is uitgeschakeld!"), grote rode UITGESCHAKELD-titel, bloed dat uit de speler spat, en de speler wordt spectator op de plek waar hij stierf
- Eliminatie uitzetten om te testen: `eliminate-on-death: false` in `config.yml`

Gemaakt voor **Paper 26.2**. Ik heb de plugin zelf niet kunnen compileren of testen, dus meld een foutmelding als de build faalt.

## 1. De plugin bouwen

**Optie A: GitHub doet het voor je (geen installatie)**
1. Maak een gratis GitHub-account en een nieuwe **public** repository, bijvoorbeeld `lowkey-smp`.
2. Upload alle bestanden en mappen uit deze map (`Add file > Upload files`). Komt `.github` niet mee? Maak dan via `Add file > Create new file` het bestand `.github/workflows/build.yml` aan en plak de inhoud erin.
3. Ga naar het tabblad **Actions**, open de laatste run en download onderaan de artifact **LowkeyCore**. Daarin zit `LowkeyCore.jar`.

**Optie B: lokaal**
Open de map in IntelliJ en draai de Gradle-taak `build`, of gebruik `gradle build`. De jar staat daarna in `build/libs/LowkeyCore.jar`.
Krijg je een fout over de Java-versie of de Paper-versie? Pas `paperApi` in `gradle.properties` of het getal in `build.gradle.kts` aan.

## 2. Het resourcepack online zetten

De server moet het pack via een link kunnen downloaden. Zet `resourcepack/lowkey-pack.zip` in dezelfde GitHub-repository (dat is al zo als je alles hebt geupload). De directe link is:

```
https://raw.githubusercontent.com/<jouw-gebruikersnaam>/<repository>/main/resourcepack/lowkey-pack.zip
```

## 3. De server instellen

1. Zet `LowkeyCore.jar` in de map `plugins/` en start de server een keer, zodat `config.yml` wordt aangemaakt.
2. Zet dit in `server.properties` en herstart:

```
resource-pack=https://raw.githubusercontent.com/<jouw-gebruikersnaam>/<repository>/main/resourcepack/lowkey-pack.zip
resource-pack-sha1=5f5c1cb8590362b24533de6db6aca69a99ec2acc
require-resource-pack=true
```

Verander je later iets aan het pack, dan verandert ook de SHA-1. Bereken die opnieuw (`sha1sum lowkey-pack.zip`) en pas hem aan.

## Later uitbreiden

Noord en Zuid krijgen straks ook een teamprefix. Een speler kan maar in een team zitten, dus de CREW-badge in de nametag moet dan samen met het eiland-icoon in een prefix komen. Zeg het als je zover bent, dan pas ik dat aan.
