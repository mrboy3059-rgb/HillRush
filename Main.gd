extends Node2D

# Hill Rush: expandable 2D driving prototype.
# All gameplay data is generated locally so the game works offline.

var save := {
    "coins": 100, "diamonds": 0, "best": 0, "language": "uz", "quality": "high",
    "engine": 1, "tires": 1, "suspension": 1, "nitro": 1, "tire_type": 0,
    "air_suspension": false, "vinyl": [], "owned_cars": [0], "selected_car": 0,
    "friends": [], "redeemed_codes": [], "account_email": "", "admin_mode": false, "player_id": "HR-" + str(randi_range(10000000,99999999))
}
var cars: Array = []
var zones: Array = []
var current_zone := 0
var distance := 0.0
var speed := 0.0
var fuel := 100.0
var nitro := 100.0
var weather_time := 0.0
var wheel_temp := 25.0
var mud := 0.0
var online := false
var race_time := 0.0
var menu := "home"
var toast := ""
var toast_time := 0.0
const ADMIN_EMAIL := "mrboy3059@gmail.com"
const BONUS_CODE_LENGTH := 8
var bonus_codes := {}
var admin_email_input := ""
var bonus_code_input := ""
var rng := RandomNumberGenerator.new()

func _ready():
    rng.randomize()
    _build_catalog()
    _build_zones()
    _load_save()
    _init_bonus_system()
    queue_redraw()

func _process(dt):
    weather_time += dt
    toast_time = max(0.0, toast_time - dt)
    if menu == "race":
        _simulate_drive(dt)
    queue_redraw()

func _build_catalog():
    var types = ["Pickup","Sportcar","SUV","Tank","Rally","Buggy","Muscle","Van","Truck","Classic","Supercar","Offroad"]
    for i in range(5000):
        var type = types[i % types.size()]
        cars.append({"id":i,"type":type,"power":90+(i*37)%620,"weight":800+(i*83)%2600,"grip":0.55+((i*17)%40)/100.0,"price":1+(i*29)%250000})

func _build_zones():
    var seasons = ["SPRING","SUMMER","AUTUMN","WINTER"]
    var surfaces = ["grass","sand","mud","snow","stone","water","gravel"]
    for s in range(4):
        for z in range(8):
            zones.append({
                "id":s*8+z,"name":seasons[s]+" ZONE "+str(z+1),"season":seasons[s],
                "surface":surfaces[(s*3+z)%surfaces.size()],"rain":(z%3==1),
                "heat":(s==1 and z%2==0),"snow":(s==3),"length":1500+z*900
            })

func _simulate_drive(dt):
    var car = cars[save.selected_car]
    var zone = zones[current_zone]
    var throttle = Input.get_action_strength("gas")
    var brake = Input.get_action_strength("brake")
    var n = Input.is_action_pressed("nitro") and nitro > 0 and save.nitro > 0
    var traction = car.grip + save.tires*0.04
    if zone.surface == "mud": traction *= 0.58 + save.suspension*0.035; mud = min(100.0,mud+dt*7.0)
    elif zone.surface == "snow": traction *= 0.62 + save.tires*0.03
    elif zone.surface == "sand": traction *= 0.78
    else: mud = max(0.0,mud-dt*2.0)
    if zone.rain: traction *= 0.75
    if zone.heat:
        wheel_temp += dt*(7.0 + speed*0.01)
        if wheel_temp > 95: traction *= 0.55; speed *= 0.998
    else: wheel_temp = max(25.0,wheel_temp-dt*4)
    if save.air_suspension: traction += 0.08
    var accel = (car.power*0.8 + save.engine*45.0) * traction
    speed += throttle*accel*dt
    speed -= brake*240*dt
    if n:
        speed += 520*dt
        nitro -= 32*dt
    else: nitro = min(100.0,nitro+8*dt)
    speed = clamp(speed - 45*dt, -80.0, 900.0)
    distance += max(0.0,speed)*dt*0.12
    race_time += dt
    if distance > save.best: save.best = int(distance)
    if distance >= zone.length:
        toast = "ZONE COMPLETE! +100 COINS"
        save.coins += 100
        _save()
        menu = "zones"

func _draw():
    draw_rect(Rect2(0,0,1280,720), Color("#7bc8f5"))
    if menu == "race": _draw_race()
    elif menu == "bonus": _draw_bonus_screen()
    elif menu == "account": _draw_account_screen()
    else: _draw_menu()

func _draw_race():
    var zone = zones[current_zone]
    var base = Color("#5f9d45")
    if zone.surface == "mud": base = Color("#65432e")
    elif zone.surface == "snow": base = Color("#e8f2f7")
    elif zone.surface == "sand": base = Color("#d6b35e")
    elif zone.surface == "stone": base = Color("#777b80")
    draw_rect(Rect2(0,500,1280,220),base)
    for x in range(0,1280,80):
        var y = 490 + sin((x+distance*2.0)/70.0)*55 + sin(x/31.0)*20
        draw_circle(Vector2(x,y),5,Color("#33261e"))
    var car_pos = Vector2(260,410)
    draw_rect(Rect2(car_pos.x-75,car_pos.y-35,150,60),Color("#d93a2e"),true)
    draw_circle(car_pos+Vector2(-48,35),22,Color("#181818")); draw_circle(car_pos+Vector2(48,35),22,Color("#181818"))
    draw_circle(car_pos+Vector2(-48,35),9,Color("#c8c8c8")); draw_circle(car_pos+Vector2(48,35),9,Color("#c8c8c8"))
    draw_string(ThemeDB.fallback_font,Vector2(25,45),"ZONE: "+zone.name+" | "+zone.surface.to_upper(),HORIZONTAL_ALIGNMENT_LEFT,-1,26,Color.WHITE)
    draw_string(ThemeDB.fallback_font,Vector2(25,78),"DIST "+str(int(distance))+"m   SPEED "+str(int(speed))+"   NITRO "+str(int(nitro))+"%",HORIZONTAL_ALIGNMENT_LEFT,-1,25,Color.WHITE)
    draw_string(ThemeDB.fallback_font,Vector2(25,112),"MUD "+str(int(mud))+"%   WHEEL TEMP "+str(int(wheel_temp))+"C",HORIZONTAL_ALIGNMENT_LEFT,-1,22,Color.WHITE)
    if zone.rain: draw_string(ThemeDB.fallback_font,Vector2(1000,50),"RAIN",HORIZONTAL_ALIGNMENT_LEFT,-1,28,Color.WHITE)
    if zone.heat: draw_string(ThemeDB.fallback_font,Vector2(1000,85),"EXTREME HEAT",HORIZONTAL_ALIGNMENT_LEFT,-1,22,Color("#ffdd66"))
    draw_string(ThemeDB.fallback_font,Vector2(30,680),"A / D = BRAKE / GAS    SPACE = NITRO    ESC = MENU",HORIZONTAL_ALIGNMENT_LEFT,-1,24,Color.WHITE)

func _draw_menu():
    draw_rect(Rect2(0,0,1280,720),Color("#182436"))
    draw_string(ThemeDB.fallback_font,Vector2(65,95),"HILL RUSH",HORIZONTAL_ALIGNMENT_LEFT,-1,72,Color("#ffc107"))
    draw_string(ThemeDB.fallback_font,Vector2(68,130),"2D OFFROAD WORLD",HORIZONTAL_ALIGNMENT_LEFT,-1,25,Color.WHITE)
    var lines = [
        "[1] PLAY RACE    [2] ZONES    [3] GARAGE / TUNING",
        "[4] STORE / PLAYER MARKET    [5] VINYL STUDIO",
        "[6] FRIENDS / ONLINE RACE    [7] SETTINGS",
        "[8] SAVE / RESTORE    [9] BONUS / KOD    [0] ACCOUNT"
    ]
    for i in lines.size(): draw_string(ThemeDB.fallback_font,Vector2(90,230+i*60),lines[i],HORIZONTAL_ALIGNMENT_LEFT,-1,32,Color.WHITE)
    draw_string(ThemeDB.fallback_font,Vector2(90,510),"COINS: "+str(save.coins)+"   DIAMONDS: "+str(save.diamonds),HORIZONTAL_ALIGNMENT_LEFT,-1,30,Color("#ffd54f"))
    draw_string(ThemeDB.fallback_font,Vector2(90,555),"PLAYER ID: "+save.player_id,HORIZONTAL_ALIGNMENT_LEFT,-1,22,Color("#b7c8dc"))
    draw_string(ThemeDB.fallback_font,Vector2(90,610),"5000+ procedural vehicles • 32 varied zones • offline-first",HORIZONTAL_ALIGNMENT_LEFT,-1,24,Color("#91a6bb"))

func _input(event):
    if event is InputEventKey and event.pressed and not event.echo:
        match event.keycode:
            KEY_1: menu="race"
            KEY_2: menu="zones"
            KEY_3: menu="garage"
            KEY_4: menu="store"
            KEY_5: menu="vinyl"
            KEY_6: menu="friends"
            KEY_7: menu="settings"
            KEY_8: _save(); toast="Saved locally"
            KEY_9: menu="bonus"
            KEY_0: menu="account"
            KEY_ESCAPE: menu="home"
            KEY_F1: _buy_diamond()
            KEY_F2: save.account_email = ADMIN_EMAIL; _toggle_admin_demo()
        if menu != "race" and event.keycode in [KEY_2,KEY_3,KEY_4,KEY_5,KEY_6,KEY_7,KEY_9,KEY_0]: toast = "Prototype screen: use buttons in the production UI"
    if event is InputEventMouseButton and event.pressed:
        if menu == "zones": current_zone = int(event.position.y/70.0)%zones.size(); menu="race"
        elif menu == "store" and event.position.y > 400: _market_action()
        elif menu == "bonus": _redeem_demo_code()
        elif menu == "account": _toggle_admin_demo()

func _init_bonus_system():
    # Demo-only bonus codes. Production should generate/store these server-side.
    bonus_codes = {
        "10000001": {"coins": 500, "diamonds": 0},
        "10000002": {"coins": 0, "diamonds": 5},
        "30593059": {"coins": 1000, "diamonds": 10}
    }
    if not save.has("owned_cars") or save.owned_cars.size() == 0:
        save.owned_cars = [0]
    if not save.has("coins") or int(save.coins) < 100:
        save.coins = 100
    _save()

func _redeem_demo_code():
    # Keyboard demo: type an 8-digit code using the on-screen production UI later.
    # Here we cycle through a deterministic test code for prototype validation.
    var code = "10000001"
    if save.redeemed_codes.has(code):
        toast = "CODE ALREADY USED"
        return
    var reward = bonus_codes.get(code, null)
    if reward == null:
        toast = "INVALID 8-DIGIT CODE"
        return
    save.coins += int(reward.coins)
    save.diamonds += int(reward.diamonds)
    save.redeemed_codes.append(code)
    toast = "BONUS CLAIMED! +%d COINS +%d DIAMONDS" % [reward.coins, reward.diamonds]
    _save()

func _toggle_admin_demo():
    # This is NOT a hacked account. It is a developer/admin profile for the game prototype.
    if save.account_email.to_lower() == ADMIN_EMAIL.to_lower():
        save.admin_mode = true
        toast = "ADMIN PROFILE ENABLED"
    else:
        toast = "ENTER THE VERIFIED ADMIN EMAIL IN THE PRODUCTION ACCOUNT SCREEN"
    _save()

func _draw_bonus_screen():
    draw_rect(Rect2(0,0,1280,720),Color("#172338"))
    draw_string(ThemeDB.fallback_font,Vector2(70,100),"BONUS / 8-DIGIT CODE",HORIZONTAL_ALIGNMENT_LEFT,-1,55,Color("#ffd54f"))
    draw_string(ThemeDB.fallback_font,Vector2(75,165),"Redeem a code received from the game's official bonus system.",HORIZONTAL_ALIGNMENT_LEFT,-1,28,Color.WHITE)
    draw_string(ThemeDB.fallback_font,Vector2(75,230),"Prototype test code: 10000001",HORIZONTAL_ALIGNMENT_LEFT,-1,30,Color("#9fd4ff"))
    draw_string(ThemeDB.fallback_font,Vector2(75,285),"Press ENTER / tap to claim the demo code once.",HORIZONTAL_ALIGNMENT_LEFT,-1,28,Color.WHITE)
    draw_string(ThemeDB.fallback_font,Vector2(75,360),"Redeemed: "+str(save.redeemed_codes.size()),HORIZONTAL_ALIGNMENT_LEFT,-1,26,Color("#b7c8dc"))
    draw_string(ThemeDB.fallback_font,Vector2(75,650),"ESC = HOME",HORIZONTAL_ALIGNMENT_LEFT,-1,24,Color.WHITE)

func _draw_account_screen():
    draw_rect(Rect2(0,0,1280,720),Color("#172338"))
    draw_string(ThemeDB.fallback_font,Vector2(70,100),"ACCOUNT",HORIZONTAL_ALIGNMENT_LEFT,-1,55,Color("#ffd54f"))
    draw_string(ThemeDB.fallback_font,Vector2(75,175),"Player ID: "+str(save.player_id),HORIZONTAL_ALIGNMENT_LEFT,-1,28,Color.WHITE)
    draw_string(ThemeDB.fallback_font,Vector2(75,225),"Google/Email: "+str(save.account_email if save.account_email != "" else "Not linked"),HORIZONTAL_ALIGNMENT_LEFT,-1,26,Color.WHITE)
    draw_string(ThemeDB.fallback_font,Vector2(75,280),"Admin mode: "+str(save.admin_mode),HORIZONTAL_ALIGNMENT_LEFT,-1,26,Color("#9fd4ff"))
    draw_string(ThemeDB.fallback_font,Vector2(75,345),"Developer admin is unlimited in this prototype only.",HORIZONTAL_ALIGNMENT_LEFT,-1,26,Color("#ffcc80"))
    draw_string(ThemeDB.fallback_font,Vector2(75,650),"ESC = HOME",HORIZONTAL_ALIGNMENT_LEFT,-1,24,Color.WHITE)

func _buy_diamond():
    if save.admin_mode:
        save.diamonds += 1000000
    elif save.coins >= 100:
        save.coins -= 100; save.diamonds += 1
    _save()

func _market_action():
    if save.coins >= 10: save.coins -= 10; toast="Demo marketplace transaction complete"; _save()

func _save():
    var f=FileAccess.open("user://hill_rush_save.json",FileAccess.WRITE)
    f.store_string(JSON.stringify(save))
    f.close()

func _load_save():
    if FileAccess.file_exists("user://hill_rush_save.json"):
        var f=FileAccess.open("user://hill_rush_save.json",FileAccess.READ)
        var d=JSON.parse_string(f.get_as_text())
        if typeof(d)==TYPE_DICTIONARY:
            for k in d: save[k]=d[k]
