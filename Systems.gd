class_name HillRushSystems

# Production-facing design notes / interfaces:
# 1) Economy: 100 coins = 1 diamond. Items start at 1 coin; pneuma/tire grades use diamonds.
# 2) Nitro: 5 diamonds for entry grade, up to 1.5M diamonds for legendary tiers.
# 3) Market: player listings contain seller_id, car_id, part_id, vinyl_id, price, currency, timestamp.
# 4) Friends: immutable player IDs; friend request, accept, block, report.
# 5) Multiplayer: authoritative server should transmit race seed, checkpoints, distance, vehicle state,
#    and anti-cheat verified inputs. Voice should be a separate WebRTC/voice service.
# 6) Offline: all core driving, zones, garage and local save work without network.
# 7) Online restore: link a Google account or platform account to a server-side save; do not trust client currency.
# 8) Vinyl studio: 250+ shape/material layers; text, geometric shapes, water, fire, gradients and decals.
# 9) Weather: rain -> mud accumulation; heat -> tire temperature/softening; winter -> snow/ice; autumn -> leaves.
# 10) Zone generator: each zone has a deterministic seed, surface patches, weather windows, hazards and landmarks.
