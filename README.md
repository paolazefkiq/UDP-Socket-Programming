# Projekti: Komunikim në Rrjet me UDP (Client–Server)

## Përshkrimi

Ky projekt implementon një sistem komunikimi në rrjet duke përdorur UDP sockets ndërmjet klientëve dhe serverit. Përveç komunikimit bazë, projekti përfshin edhe menaxhimin e klientëve, ruajtjen e statistikave, një server HTTP për monitorim dhe operime me file në server.

---

## Struktura e Projektit

```
src/
│
├── client/
│   └── UDPClient.java
│
├── server/
│   ├── UDPServer.java
│   ├── ClientSession.java
│   ├── FileManager.java
│   ├── StatsStore.java
│   ├── StatsHttpServer.java
│   └── MessageLogEntry.java
│
└── Main.java
```

---

## Funksionalitetet Kryesore

### 1. UDP Server

* Pranon dhe përpunon mesazhe nga klientët
* Menaxhon klientët aktivë duke përdorur mekanizëm timeout
* Kufizon numrin maksimal të klientëve
* Integron menaxhimin e statistikave dhe file-ve

### 2. UDP Client

* Lidhet me serverin përmes UDP
* Dërgon komanda dhe mesazhe
* Merr përgjigje nga serveri

### 3. Menaxhimi i Klientëve

* Çdo klient identifikohet me një çelës unik
* Ruhen të dhëna si IP, port dhe koha e fundit aktive
* Klientët joaktivë largohen automatikisht

### 4. Statistikat

* Ruhet numri i klientëve aktivë
* Numri i mesazheve të dërguara
* Historiku i mesazheve

### 5. HTTP Server për Statistika

* Ekzekutohet në portin 8081
* Endpoint:
  http://localhost:8081/stats
* Ofron informacion mbi gjendjen e serverit dhe aktivitetin

### 6. Menaxhimi i File-ve

* Operon mbi file në folderin server_files
* Mbështet operime bazë mbi file
* Përfshin kontroll për qasje administrative

---

## Si të Ekzekutohet Projekti

### 1. Startimi i Serverit

Ekzekuto:

```
UDPServer.java
```

### 2. Aksesimi i Statistikave

Hap në browser:

```
http://localhost:8081/stats
```

### 3. Startimi i Klientit

Ekzekuto:

```
UDPClient.java
```

---

## Parametrat Kryesorë

| Parametri         | Vlera      |
| ----------------- | ---------- |
| Porti i Serverit  | 5051       |
| Porti HTTP        | 8081       |
| Numri max klientë | 10         |
| Timeout           | 30 sekonda |

---

## Teknologjitë e Përdorura

* Java
* UDP Sockets
* HTTP Server i integruar në Java
* Struktura për menaxhim paralel (thread-safe)

---

## Qëllimi i Projektit

Qëllimi i këtij projekti është të demonstrojë:

* Komunikimin në rrjet përmes UDP
* Ndërtimin e një sistemi client-server
* Menaxhimin e klientëve dhe burimeve
* Integrimin e shërbimeve shtesë si monitorimi dhe file management

---

## Shënime

* Projekti është për qëllime edukative
* UDP nuk garanton dorëzimin e mesazheve
* Nuk përdoret TCP në këtë implementim
