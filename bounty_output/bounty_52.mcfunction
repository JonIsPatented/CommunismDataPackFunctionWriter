execute as @e[tag=Overseer,limit=1] store result score @s item_count run data get block -396 76 -65 Items[{id:"minecraft:redstone"}].Count
scoreboard players set @e[tag=Overseer] multiplier 4

scoreboard players operation @e[tag=Overseer] item_count *= @e[tag=Overseer,limit=1] multiplier
execute positioned -395.5 75.00 -60.5 run scoreboard players operation @a[distance=0..2,limit=1] generosity += @e[tag=Overseer] item_count
execute positioned -395.5 75.00 -60.5 run scoreboard players operation @a[distance=0..2,limit=1] communist-points += @e[tag=Overseer] item_count
scoreboard players set @e[tag=Overseer] item_count 0

loot insert -385 76 -51 mine -396 76 -65 minecraft:air{drop_contents:1b}
data remove block -396 76 -65 Items[{id:"minecraft:redstone"}]