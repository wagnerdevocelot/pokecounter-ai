# PokeCounterAI

A web application that generates Pokemon counter builds using AI. This tool helps competitive Pokemon players find effective counters to opponent teams by leveraging OpenAI's API and Smogon's competitive Pokemon resources.

## Features

- Input up to 6 Pokemon builds in Pokemon Showdown format
- Select the Pokemon generation (Gen 1-9)
- Choose the competitive format (OU, Ubers, UU, etc.)
- Generate AI-powered counter recommendations using web search for up-to-date information
- One precise counter for each input Pokemon in the same order
- Easy copy-to-clipboard functionality for importing into Pokemon Showdown

---

- Input
![image](https://github.com/user-attachments/assets/8dae87ef-28c3-49cb-b6f4-5b9beff0b084)

- Output
![image](https://github.com/user-attachments/assets/674a628e-98ed-4425-975d-90081d199679)

---

## Prerequisites

You will need:
- [Leiningen][] 2.0.0 or above installed
- An OpenAI API key

[leiningen]: https://github.com/technomancy/leiningen

## Setup

1. Clone this repository
2. Set your OpenAI API key as an environment variable:
   ```
   export OPENAI_API_KEY=your_openai_api_key_here
   ```

## Running

To start a web server for the application, run:

```
cd pokecounter-ai
lein ring server
```

This will start the server on port 3000 and automatically open your browser to the application.

## Usage

1. Enter up to 6 Pokemon builds in Pokemon Showdown format in the text area
2. Select the Pokemon generation from the dropdown
3. Choose the competitive format from the dropdown
4. Click "Generate Counters"
5. View the AI-generated counter suggestions
6. Use the "Copy to Clipboard" button to copy the results for use in Pokemon Showdown

## Example

### Input
```
Dondozo @ Leftovers
Ability: Unaware
Tera Type: Dragon
EVs: 252 HP / 252 Def / 4 SpD
Impish Nature
- Body Press
- Rest
- Sleep Talk
- Curse
```

### Generated Counter (example)
```
Toxapex @ Black Sludge
Ability: Regenerator
EVs: 252 HP / 252 Def / 4 SpD
Bold Nature
- Scald
- Toxic
- Recover
- Haze
```

You can input up to 6 Pokemon builds, and the system will generate exactly one counter for each. Each counter is specifically chosen to deal with the corresponding Pokemon's moves, ability, and typical strategy.

## References

The AI uses the following Smogon resources to generate counters:
- [Smogon Forums](https://www.smogon.com/forums/)
- [Smogon Articles](https://www.smogon.com/articles/)
- [Smogon Pokedex](https://www.smogon.com/dex/sv/pokemon/)
