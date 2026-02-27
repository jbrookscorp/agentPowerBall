use rand::Rng;
use std::io;

struct PowerBallGame {
    main_numbers: Vec<u32>,
    powerball: u32,
}

impl PowerBallGame {
    fn new() -> Self {
        let mut rng = rand::thread_rng();
        
        // Generate 5 unique numbers from 1 to 69
        let mut main_numbers: Vec<u32> = (1..=69).collect();
        let mut selected_numbers = Vec::new();
        
        for _ in 0..5 {
            let index = rng.gen_range(0..main_numbers.len());
            selected_numbers.push(main_numbers.remove(index));
        }
        selected_numbers.sort();
        
        // Generate powerball number from 1 to 26
        let powerball = rng.gen_range(1..=26);
        
        PowerBallGame {
            main_numbers: selected_numbers,
            powerball,
        }
    }
    
    fn display(&self) {
        print!("Main Numbers: ");
        for (i, num) in self.main_numbers.iter().enumerate() {
            print!("{}", num);
            if i < self.main_numbers.len() - 1 {
                print!(", ");
            }
        }
        println!("  PowerBall: {}", self.powerball);
    }
}

fn main() {
    println!("=== PowerBall Quick Pick Generator ===");
    println!();
    
    loop {
        println!("How many games would you like to generate? (Enter 0 to quit)");
        
        let mut input = String::new();
        io::stdin()
            .read_line(&mut input)
            .expect("Failed to read input");
        
        let num_games: u32 = match input.trim().parse() {
            Ok(num) => num,
            Err(_) => {
                println!("Invalid input. Please enter a valid number.");
                continue;
            }
        };
        
        if num_games == 0 {
            println!("Thanks for using PowerBall Quick Pick Generator. Good luck!");
            break;
        }
        
        println!();
        println!("=== Your {} Quick Pick{} ===", num_games, if num_games > 1 { "s" } else { "" });
        
        for i in 1..=num_games {
            let game = PowerBallGame::new();
            print!("Game {}: ", i);
            game.display();
        }
        
        println!();
    }
}
