use rand::Rng;
use rand::seq::SliceRandom; // Add this import for shuffle
use std::io;
use std::collections::HashMap;

struct PowerBallGame {
    main_numbers: Vec<u32>,
    powerball: u32,
}

// Enhanced struct for pattern-based generation
struct EnhancedPowerBallGenerator {
    main_weights: HashMap<u32, f64>,
    powerball_weights: HashMap<u32, f64>,
}

impl EnhancedPowerBallGenerator {
    fn new() -> Self {
        let mut main_weights = HashMap::new();
        let mut powerball_weights = HashMap::new();
        
        // Weights based on analysis of 49 PowerBall drawings
        // Numbers with higher frequency get higher weights
        
        // Hot main numbers (from analysis) with elevated weights
        let hot_numbers = [28, 5, 51, 58, 8, 18, 19, 53, 6, 21];
        for &num in &hot_numbers {
            main_weights.insert(num, 3.0); // Hot numbers get 3x weight
        }
        
        // Regular weights for other numbers
        for num in 1..=69 {
            if !main_weights.contains_key(&num) {
                // Cold numbers get lower weight
                let cold_numbers = [61, 65, 12, 13, 17, 38, 41, 42, 45, 67];
                if cold_numbers.contains(&num) {
                    main_weights.insert(num, 0.5); // Cold numbers get 0.5x weight
                } else {
                    main_weights.insert(num, 1.0); // Regular weight
                }
            }
        }
        
        // PowerBall weights - PowerBall 2 is extremely hot (55.1%)
        powerball_weights.insert(2, 5.0); // Very hot
        powerball_weights.insert(3, 2.0); // Hot
        powerball_weights.insert(4, 1.5); // Warm
        powerball_weights.insert(5, 1.2); // Slightly warm
        
        // Cold powerballs (never appeared in dataset)
        for num in 1..=26 {
            if !powerball_weights.contains_key(&num) {
                let cold_powerballs = [22, 23, 24, 25, 26];
                if cold_powerballs.contains(&num) {
                    powerball_weights.insert(num, 0.1); // Very cold
                } else {
                    powerball_weights.insert(num, 0.8); // Regular cold
                }
            }
        }
        
        EnhancedPowerBallGenerator {
            main_weights,
            powerball_weights,
        }
    }
    
    fn weighted_random_main(&self, rng: &mut impl rand::Rng, excluded: &[u32]) -> u32 {
        let available_weights: Vec<(u32, f64)> = self.main_weights
            .iter()
            .filter(|(&num, _)| !excluded.contains(&num))
            .map(|(&num, &weight)| (num, weight))
            .collect();
        
        let total_weight: f64 = available_weights.iter().map(|(_, w)| w).sum();
        let mut random_weight = rng.gen::<f64>() * total_weight;
        
        for (num, weight) in &available_weights {
            random_weight -= weight;
            if random_weight <= 0.0 {
                return *num;
            }
        }
        
        // Fallback
        available_weights[rng.gen_range(0..available_weights.len())].0
    }
    
    fn weighted_random_powerball(&self, rng: &mut impl rand::Rng) -> u32 {
        let total_weight: f64 = self.powerball_weights.values().sum();
        let mut random_weight = rng.gen::<f64>() * total_weight;
        
        for (&number, &weight) in &self.powerball_weights {
            random_weight -= weight;
            if random_weight <= 0.0 {
                return number;
            }
        }
        
        // Fallback
        rng.gen_range(1..=26)
    }
}

impl PowerBallGame {
    // Original random generator
    fn new() -> Self {
        let mut rng = rand::thread_rng();
        
        let mut main_numbers: Vec<u32> = (1..=69).collect();
        let mut selected_numbers = Vec::new();
        
        for _ in 0..5 {
            let index = rng.gen_range(0..main_numbers.len());
            selected_numbers.push(main_numbers.remove(index));
        }
        selected_numbers.sort();
        
        let powerball = rng.gen_range(1..=26);
        
        PowerBallGame {
            main_numbers: selected_numbers,
            powerball,
        }
    }
    
    // Enhanced pattern-based generator
    fn new_pattern_based() -> Self {
        let mut rng = rand::thread_rng();
        let generator = EnhancedPowerBallGenerator::new();
        
        let mut selected_numbers = Vec::new();
        let mut excluded = Vec::new();
        
        // Strategy: Mix hot and cold numbers
        // 2-3 hot numbers, 1-2 cold numbers, rest regular
        
        let hot_numbers = [28, 5, 51, 58, 8, 18, 19, 53, 6, 21];
        let cold_numbers = [61, 65, 12, 13, 17, 38, 41, 42, 45, 67];
        
        // Select 2-3 hot numbers
        let hot_count = rng.gen_range(2..=3);
        let mut hot_candidates = hot_numbers.to_vec();
        hot_candidates.shuffle(&mut rng);
        
        for _ in 0..hot_count {
            if let Some(&hot_num) = hot_candidates.first() {
                if !excluded.contains(&hot_num) {
                    selected_numbers.push(hot_num);
                    excluded.push(hot_num);
                    hot_candidates.remove(0);
                }
            }
        }
        
        // Select 1-2 cold numbers
        let cold_count = rng.gen_range(1..=2);
        let mut cold_candidates = cold_numbers.to_vec();
        cold_candidates.shuffle(&mut rng);
        
        for _ in 0..cold_count {
            if let Some(&cold_num) = cold_candidates.first() {
                if !excluded.contains(&cold_num) {
                    selected_numbers.push(cold_num);
                    excluded.push(cold_num);
                    cold_candidates.remove(0);
                }
            }
        }
        
        // Fill remaining slots with weighted random selection
        while selected_numbers.len() < 5 {
            let num = generator.weighted_random_main(&mut rng, &excluded);
            selected_numbers.push(num);
            excluded.push(num);
        }
        
        selected_numbers.sort();
        
        // PowerBall selection - heavily weighted towards hot numbers
        let powerball = generator.weighted_random_powerball(&mut rng);
        
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
    
    // Check if the combination matches target criteria
    fn meets_criteria(&self) -> bool {
        let sum: u32 = self.main_numbers.iter().sum();
        let range = self.main_numbers.last().unwrap() - self.main_numbers.first().unwrap();
        
        // Target: Sum 134-218, Range 41-51
        sum >= 134 && sum <= 218 && range >= 41 && range <= 51
    }
}

fn main() {
    println!("=== PowerBall Quick Pick Generator ===");
    println!("Enhanced with Pattern Analysis from 49 Historical Drawings");
    println!();
    
    loop {
        println!("Choose generation mode:");
        println!("1. Pattern-Based (Recommended - uses historical analysis)");
        println!("2. Pure Random (Original algorithm)");
        println!("3. Pattern-Based with Criteria Filtering");
        println!("Enter 0 to quit");
        
        let mut input = String::new();
        io::stdin()
            .read_line(&mut input)
            .expect("Failed to read input");
        
        let mode: u32 = match input.trim().parse() {
            Ok(num) => num,
            Err(_) => {
                println!("Invalid input. Please enter 0, 1, 2, or 3.");
                continue;
            }
        };
        
        if mode == 0 {
            println!("Thanks for using PowerBall Quick Pick Generator. Good luck!");
            break;
        }
        
        if mode > 3 {
            println!("Invalid mode. Please enter 0, 1, 2, or 3.");
            continue;
        }
        
        println!("How many games would you like to generate?");
        
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
        
        let mode_name = match mode {
            1 => "Pattern-Based",
            2 => "Pure Random",
            3 => "Pattern-Based with Criteria",
            _ => "Unknown",
        };
        
        println!();
        println!("=== Your {} Quick Pick{} ({}) ===", num_games, 
                 if num_games > 1 { "s" } else { "" }, mode_name);
        
        let mut games_generated = 0;
        let mut attempts = 0;
        
        while games_generated < num_games {
            let game = match mode {
                1 => PowerBallGame::new_pattern_based(),
                2 => PowerBallGame::new(),
                3 => {
                    // Keep generating until we find one that meets criteria
                    loop {
                        attempts += 1;
                        let candidate = PowerBallGame::new_pattern_based();
                        if candidate.meets_criteria() {
                            break candidate;
                        }
                    }
                },
                _ => PowerBallGame::new_pattern_based(),
            };
            
            print!("Game {}: ", games_generated + 1);
            game.display();
            
            if mode == 3 {
                let sum: u32 = game.main_numbers.iter().sum();
                let range = game.main_numbers.last().unwrap() - game.main_numbers.first().unwrap();
                println!("       Sum: {} Range: {} (Target: 134-218 sum, 41-51 range)", sum, range);
            }
            
            games_generated += 1;
        }
        
        if mode == 3 {
            println!("Total attempts to meet criteria: {}", attempts);
        }
        
        println!();
    }
}
