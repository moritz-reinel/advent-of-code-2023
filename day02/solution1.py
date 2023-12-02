from itertools import combinations
import re

games = {}
pattern = re.compile(r'Game (\d+)')
pattern_red = re.compile(r'(\d+) red')
pattern_blue = re.compile(r'(\d+) blue')
pattern_green = re.compile(r'(\d+) green')

target_sums = [12, 13, 14]

possible = 0

with open("input.txt", mode="r", encoding="UTF-8") as fd:
    for line in fd.readlines():
        game_id = pattern.search(line).group(1)
        
        reds = [int(n) for n in pattern_red.findall(line)]
        greens = [int(n) for n in pattern_green.findall(line)]
        blues = [int(n) for n in pattern_blue.findall(line)]

        if max(reds) <= 12 and max(greens) <= 13 and max(blues) <= 14:
            possible += int(game_id)

print(possible)

