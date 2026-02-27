import csv
from collections import Counter
import statistics

def analyze_powerball_data(csv_file):
    with open(csv_file, 'r') as file:
        reader = csv.DictReader(file)
        data = list(reader)
    
    main_numbers_counter = Counter()
    powerball_counter = Counter()
    main_numbers_by_position = [Counter() for _ in range(5)]
    number_sum_list = []
    number_ranges = []
    
    print(f"Analyzing {len(data)} PowerBall drawings...")
    print("=" * 60)
    
    for row in data:
        main_numbers_str = row['Main Numbers']
        main_numbers = [int(x.strip()) for x in main_numbers_str.split(',')]
        powerball = int(row['Powerball'])
        
        for i, num in enumerate(main_numbers[:5]):
            main_numbers_counter[num] += 1
            main_numbers_by_position[i][num] += 1
        
        powerball_counter[powerball] += 1
        
        main_sum = sum(main_numbers[:5])
        number_sum_list.append(main_sum)
        
        num_range = max(main_numbers[:5]) - min(main_numbers[:5])
        number_ranges.append(num_range)
    
    print("\n1. MOST FREQUENT MAIN NUMBERS (Top 10):")
    print("-" * 60)
    for num, count in main_numbers_counter.most_common(10):
        percentage = (count / len(data)) * 100
        print(f"Number {num}: {count} times ({percentage:.1f}%)")
    
    print("\n2. MOST FREQUENT POWERBALL NUMBERS (Top 10):")
    print("-" * 60)
    for num, count in powerball_counter.most_common(10):
        percentage = (count / len(data)) * 100
        print(f"PowerBall {num}: {count} times ({percentage:.1f}%)")
    
    print("\n3. NUMBER DISTRIBUTION BY POSITION:")
    print("-" * 60)
    for i in range(5):
        print(f"Position {i+1} (smallest to largest):")
        for num, count in main_numbers_by_position[i].most_common(5):
            print(f"  {num}: {count} times")
    
    print("\n4. SUM STATISTICS (Main Numbers):")
    print("-" * 60)
    print(f"Average Sum: {statistics.mean(number_sum_list):.1f}")
    print(f"Median Sum: {statistics.median(number_sum_list):.1f}")
    print(f"Min Sum: {min(number_sum_list)}")
    print(f"Max Sum: {max(number_sum_list)}")
    print(f"Standard Deviation: {statistics.stdev(number_sum_list):.1f}")
    
    print("\n5. RANGE STATISTICS (Max - Min):")
    print("-" * 60)
    print(f"Average Range: {statistics.mean(number_ranges):.1f}")
    print(f"Median Range: {statistics.median(number_ranges):.1f}")
    print(f"Min Range: {min(number_ranges)}")
    print(f"Max Range: {max(number_ranges)}")
    
    print("\n6. HOT AND COLD NUMBERS:")
    print("-" * 60)
    all_numbers_freq = [(num, main_numbers_counter[num]) for num in range(1, 70)]
    all_numbers_freq.sort(key=lambda x: x[1], reverse=True)
    
    print("HOT NUMBERS (Most Frequent):")
    for num, count in all_numbers_freq[:10]:
        print(f"  {num}: {count} times")
    
    print("\nCOLD NUMBERS (Least Frequent):")
    for num, count in all_numbers_freq[-10:]:
        print(f"  {num}: {count} times")
    
    print("\n7. POWERBALL HOT AND COLD:")
    print("-" * 60)
    all_powerball_freq = [(num, powerball_counter[num]) for num in range(1, 27)]
    all_powerball_freq.sort(key=lambda x: x[1], reverse=True)
    
    print("HOT POWERBALLS:")
    for num, count in all_powerball_freq[:5]:
        print(f"  {num}: {count} times")
    
    print("\nCOLD POWERBALLS:")
    for num, count in all_powerball_freq[-5:]:
        print(f"  {num}: {count} times")
    
    print("\n8. NUMBER PAIRS FREQUENCY:")
    print("-" * 60)
    pair_counter = Counter()
    for row in data:
        main_numbers = [int(x.strip()) for x in row['Main Numbers'].split(',')][:5]
        for i in range(len(main_numbers)):
            for j in range(i+1, len(main_numbers)):
                pair = tuple(sorted([main_numbers[i], main_numbers[j]]))
                pair_counter[pair] += 1
    
    print("Most Common Number Pairs:")
    for pair, count in pair_counter.most_common(10):
        print(f"  {pair[0]} & {pair[1]}: {count} times")
    
    total_drawings = len(data)
    number_weights = {}
    for num in range(1, 70):
        frequency = main_numbers_counter[num]
        weight = 1.0 + (frequency / total_drawings) * 2.0
        number_weights[num] = weight
    
    powerball_weights = {}
    for num in range(1, 27):
        frequency = powerball_counter[num]
        weight = 1.0 + (frequency / total_drawings) * 2.0
        powerball_weights[num] = weight
    
    print("\n" + "=" * 60)
    print("PATTERN ANALYSIS SUMMARY FOR WEIGHTED ALGORITHM:")
    print("=" * 60)
    print("\nRecommended Strategy:")
    print(f"1. Target Sum Range: {int(statistics.mean(number_sum_list) - statistics.stdev(number_sum_list))} - {int(statistics.mean(number_sum_list) + statistics.stdev(number_sum_list))}")
    print(f"2. Target Number Range: {int(statistics.mean(number_ranges) - 5)} - {int(statistics.mean(number_ranges) + 5)}")
    print("3. Include 2-3 numbers from HOT list")
    print("4. Include 1-2 numbers from COLD list")
    print("5. Consider number positions (smaller numbers tend to appear in early positions)")
    print("6. Use weighted probability for PowerBall selection")
    
    return {
        'total_drawings': total_drawings,
        'main_numbers_weights': number_weights,
        'powerball_weights': powerball_weights,
        'avg_sum': statistics.mean(number_sum_list),
        'std_sum': statistics.stdev(number_sum_list),
        'avg_range': statistics.mean(number_ranges),
        'hot_numbers': [num for num, count in all_numbers_freq[:15]],
        'cold_numbers': [num for num, count in all_numbers_freq[-15:]],
        'hot_powerball': [num for num, count in all_powerball_freq[:5]],
        'cold_powerball': [num for num, count in all_powerball_freq[-5:]],
        'position_preferences': [counter.most_common(3) for counter in main_numbers_by_position]
    }

if __name__ == "__main__":
    csv_file = "/home/josh/Development/Sandbox/PoC/Rust/agentPowerBall/powerball_generator/resources/powerball.csv"
    results = analyze_powerball_data(csv_file)
    print("\n" + "=" * 60)
    print("ANALYSIS COMPLETE!")
    print("=" * 60)
    print(f"\nAnalyzed {results['total_drawings']} PowerBall drawings")
