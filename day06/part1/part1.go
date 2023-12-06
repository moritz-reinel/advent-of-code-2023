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

	times := strings.Fields(lines[0])[1:]
	distances := strings.Fields(lines[1])[1:]

	sum := 1
	for idx, item := range times {
		time, err := strconv.Atoi(item)
		if err != nil {
			panic(err)
		}
		distance, err := strconv.Atoi(distances[idx])
		if err != nil {
			panic(err)
		}

		beat := 0
		for t := 1; t <= time; t++ {
			possibleDistance := (time - t) * t
			if possibleDistance > distance {
				beat++
			}
		}

		sum *= beat
	}

	fmt.Println(sum)
}
