# Hill Rush — Expanded 2D Prototype

Bu paket oldingi starterdan kengaytirilgan **offline-first Godot 4 prototip**.

## Hozirgi prototipda
- 5000+ procedurally generated vehicle catalog
- Pickup, Sportcar, SUV, Tank, Rally, Buggy, Muscle, Van, Truck, Classic, Supercar, Offroad klasslari
- 4 fasl va 32 baza zona
- Grass, sand, mud, snow, stone, water, gravel sirtlari
- Yomg'ir → loy va grip pasayishi
- Issiq → wheel temperature oshishi va shina yumshashi/grip pasayishi
- Snow/winter traction
- Nitro, engine, tires, suspension va air/pneuma konsepsiyasi
- Coin/diamond economy: 100 coin = 1 diamond
- Nitro narx tierlari 5 diamonddan 1.5M diamondgacha dizayn qilingan
- Player ID va friends tizimi uchun interfeys/ma'lumot modeli
- Marketplace/store arxitekturasi
- 250+ vinyl layer uchun arxitektura: text, geometry, fire, water va boshqa layerlar
- Offline local save
- Multiplayer race uchun authoritative-server arxitekturasi tavsifi

## Muhim texnik chegara
Bu ZIP ichida to'liq MMO backend, real-time server, voice server, anti-cheat yoki 5000 ta qo'lda chizilgan avtomobil modeli yo'q.
Ularni bitta chatda haqiqiy production darajasida yaratib bo'lmaydi. 5000+ mashina catalogi procedural data sifatida yaratilgan; real rasmlar/sprite va litsenziyalangan nom/logolar keyin asset pipeline orqali qo'shiladi.

## Ishga tushirish
Godot 4.x bilan `project.godot` ni oching va Run bosing.

Klaviatura:
1 Play
2 Zones
3 Garage
4 Store
5 Vinyl Studio
6 Friends/Online
7 Settings
8 Save
ESC Home
A/D Brake/Gas
SPACE Nitro

## Production roadmap
1. Godot Character/RigidBody2D + custom suspension physics
2. TileMap terrain + destructible mud/snow/water patches
3. Vehicle sprite/animation asset pipeline
4. Dedicated multiplayer server (race seed + verified inputs)
5. Account/cloud save backend
6. Marketplace transaction service
7. WebRTC voice chat
8. Moderation/reporting and anti-cheat
9. Full Uzbek/Russian/English localization
10. Android/iOS optimization and Google Play release pipeline


## New bonus/admin changes
- New player starts with **1 free Stock car + 100 coins**.
- Added 8-digit Bonus Code system with one-time redemption tracking.
- Added an official developer/admin profile concept using `mrboy3059@gmail.com` as the verified admin email identifier in this prototype.
- Admin mode can grant unlimited prototype currency; it is deliberately implemented as a game-owner/admin feature, not a hacked/broken account.
- Production version should verify the email server-side and generate bonus codes on a backend. Never trust client-side currency for an online economy.
- Demo test shortcut: `F2` assigns the admin email in the prototype. `F1` tests the admin currency grant.
