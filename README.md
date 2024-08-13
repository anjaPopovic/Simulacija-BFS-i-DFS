# Simulacija-BFS-i-DFS
Projekat je jednostavna interaktivna JavaFX simulacija algoritama pretrage po širini (BFS) i po dubini (DFS) na grafovima.

## 1. Uvod

Ovaj projekat razvijen je u okviru predmeta CS203 - Algoritmi i strukture podataka, i predstavlja simulaciju algoritama pretrage po širini (BFS) i po dubini (DFS) na grafovima. Projekat je razvijen u IntelliJ IDEA razvojnom okruženju, koristeći JavaFX.

Aplikacija omogućava korisnicima da interaktivno kreiraju grafove, dodaju i povezuju čvorove, i da vizualizuju rezultate pretrage BFS i DFS algoritmima. Korisnici mogu obrisati čvorove i resetovati scenu kako bi krenuli ispočetka.

## 2. Arhitektura

Aplikacija je podeljena u nekoliko ključnih komponenti, koje obuhvataju manipulaciju grafovima, obradu događaja korisnika, i implementaciju BFS i DFS algoritama. 

## 3. Metode aplikacije

Aplikacija sadrži niz metoda koje olakšavaju rad sa grafom:

- **handleMouseClick:** Obrada klikova miša za kreiranje i selektovanje čvorova.
- **addNode:** Dodavanje novog čvora na graf.
- **handleMousePressed/Released:** Kreiranje grana između čvorova.
- **deleteNode:** Brisanje čvora i njegovih grana.
- **resetGraph:** Resetovanje cele scene.
- **bfs/dfs:** Implementacija BFS i DFS algoritama.
- **showAlert:** Prikaz rezultata pretrage u alert prozoru.

## 4. Izgled aplikacije

Početna stranica aplikacije:

![image](https://github.com/user-attachments/assets/73594172-8329-448e-8691-2770cf0cc9cb)


## 3. Zaključak

Ovaj projekat uspešno demonstrira implementaciju i simulaciju BFS i DFS algoritama na grafovima, omogućavajući korisnicima interaktivno iskustvo u radu sa grafovima. Projekat zadovoljava sve zahteve definisane u okviru zadatka za predmet CS203.

