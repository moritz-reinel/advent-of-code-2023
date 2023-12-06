package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	content, err := os.ReadFile("../input.txt")
	if err != nil {
		panic(err)
	}

	lines := strings.Split(strings.TrimSpace(string(content)), "\n")

	time, err := strconv.Atoi(
		strings.ReplaceAll(strings.Split(lines[0], ":")[1], " ", ""))
	if err != nil {
		panic(err)
	}

	distance, err := strconv.Atoi(
		strings.ReplaceAll(strings.Split(lines[1], ":")[1], " ", ""))
	if err != nil {
		panic(err)
	}

	sum := 0
	for t := 1; t <= time; t++ {
		possibleDistance := (time - t) * t
		if possibleDistance > distance {
			sum++
		}
	}

	fmt.Println(sum)
}
