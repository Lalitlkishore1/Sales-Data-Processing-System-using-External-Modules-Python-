import csv
import os
import statistics

FILE_NAME = "sales.csv"
FIELDS = ["Product", "Quantity", "Price"]


def initialize_file():
    if not os.path.exists(FILE_NAME):
        with open(FILE_NAME, "w", newline="") as file:
            writer = csv.writer(file)
            writer.writerow(FIELDS)


def load_sales():
    sales = []

    if not os.path.exists(FILE_NAME):
        return sales

    with open(FILE_NAME, "r", newline="") as file:
        reader = csv.reader(file)
        rows = list(reader)

    if not rows:
        return sales

    if rows[0] == FIELDS:
        rows = rows[1:]

    for row in rows:
        if len(row) != 3:
            continue

        try:
            product = row[0].strip()
            quantity = int(row[1])
            price = float(row[2])

            if quantity < 0 or price < 0:
                continue

            sales.append({
                "Product": product,
                "Quantity": quantity,
                "Price": price,
                "Amount": quantity * price
            })
        except ValueError:
            continue

    return sales


def save_sales(sales):
    with open(FILE_NAME, "w", newline="") as file:
        writer = csv.writer(file)
        writer.writerow(FIELDS)
        for item in sales:
            writer.writerow([item["Product"], item["Quantity"], item["Price"]])


def add_sale():
    product = input("Enter Product Name: ").strip()
    quantity = input("Enter Quantity: ").strip()
    price = input("Enter Price: ").strip()

    if not product or not quantity or not price:
        print("All fields are required.")
        return

    try:
        quantity = int(quantity)
        price = float(price)

        if quantity < 0 or price < 0:
            print("Quantity and price must be positive.")
            return

    except ValueError:
        print("Invalid quantity or price.")
        return

    with open(FILE_NAME, "a", newline="") as file:
        writer = csv.writer(file)
        writer.writerow([product, quantity, price])

    print("Sales record added successfully!")


def view_sales():
    sales = load_sales()

    if not sales:
        print("No sales records found.")
        return

    print("\n--- Sales Records ---")
    print(f"{'Product':<20}{'Quantity':<10}{'Price':<12}{'Amount':<12}")
    print("-" * 54)

    for item in sales:
        print(f"{item['Product']:<20}{item['Quantity']:<10}{item['Price']:<12.2f}{item['Amount']:<12.2f}")


def total_sales():
    sales = load_sales()

    if not sales:
        print("No sales data available.")
        return

    total = sum(item["Amount"] for item in sales)
    print(f"Total Sales Amount: {total:.2f}")


def average_sales():
    sales = load_sales()

    if not sales:
        print("No sales data available.")
        return

    amounts = [item["Amount"] for item in sales]
    print(f"Average Sales Amount: {statistics.mean(amounts):.2f}")


def highest_lowest_sales():
    sales = load_sales()

    if not sales:
        print("No sales data available.")
        return

    highest = max(sales, key=lambda x: x["Amount"])
    lowest = min(sales, key=lambda x: x["Amount"])

    print("\n--- Highest Sale ---")
    print(f"Product : {highest['Product']}")
    print(f"Quantity: {highest['Quantity']}")
    print(f"Price   : {highest['Price']:.2f}")
    print(f"Amount  : {highest['Amount']:.2f}")

    print("\n--- Lowest Sale ---")
    print(f"Product : {lowest['Product']}")
    print(f"Quantity: {lowest['Quantity']}")
    print(f"Price   : {lowest['Price']:.2f}")
    print(f"Amount  : {lowest['Amount']:.2f}")


def search_product():
    sales = load_sales()

    if not sales:
        print("No sales data available.")
        return

    keyword = input("Enter product name to search: ").strip().lower()
    found = False

    print("\n--- Search Results ---")
    print(f"{'Product':<20}{'Quantity':<10}{'Price':<12}{'Amount':<12}")
    print("-" * 54)

    for item in sales:
        if keyword in item["Product"].lower():
            print(f"{item['Product']:<20}{item['Quantity']:<10}{item['Price']:<12.2f}{item['Amount']:<12.2f}")
            found = True

    if not found:
        print("Product not found.")


def update_sale():
    sales = load_sales()

    if not sales:
        print("No sales records found.")
        return

    product_name = input("Enter product name to update: ").strip().lower()
    found = False

    for item in sales:
        if item["Product"].lower() == product_name:
            found = True
            print("Current Record:")
            print(f"Product : {item['Product']}")
            print(f"Quantity: {item['Quantity']}")
            print(f"Price   : {item['Price']:.2f}")

            new_product = input("Enter New Product Name: ").strip()
            new_quantity = input("Enter New Quantity: ").strip()
            new_price = input("Enter New Price: ").strip()

            if not new_product or not new_quantity or not new_price:
                print("All fields are required.")
                return

            try:
                new_quantity = int(new_quantity)
                new_price = float(new_price)

                if new_quantity < 0 or new_price < 0:
                    print("Quantity and price must be positive.")
                    return
            except ValueError:
                print("Invalid quantity or price.")
                return

            item["Product"] = new_product
            item["Quantity"] = new_quantity
            item["Price"] = new_price
            item["Amount"] = new_quantity * new_price

            save_sales(sales)
            print("Sales record updated successfully!")
            return

    if not found:
        print("Product not found.")


def delete_sale():
    sales = load_sales()

    if not sales:
        print("No sales records found.")
        return

    product_name = input("Enter product name to delete: ").strip().lower()
    new_sales = []
    found = False

    for item in sales:
        if item["Product"].lower() == product_name:
            found = True
        else:
            new_sales.append(item)

    if found:
        save_sales(new_sales)
        print("Sales record deleted successfully!")
    else:
        print("Product not found.")


def sales_summary():
    sales = load_sales()

    if not sales:
        print("No sales data available.")
        return

    amounts = [item["Amount"] for item in sales]

    print("\n--- Sales Summary ---")
    print(f"Number of Records : {len(sales)}")
    print(f"Total Sales       : {sum(amounts):.2f}")
    print(f"Average Sales     : {statistics.mean(amounts):.2f}")
    print(f"Highest Sales     : {max(amounts):.2f}")
    print(f"Lowest Sales      : {min(amounts):.2f}")


def main():
    initialize_file()

    while True:
        print("\n===== Sales Data Processing System =====")
        print("1. Add Sales Record")
        print("2. View Sales Records")
        print("3. Calculate Total Sales")
        print("4. Calculate Average Sales")
        print("5. Find Highest and Lowest Sales")
        print("6. Search Product")
        print("7. Update Sales Record")
        print("8. Delete Sales Record")
        print("9. Sales Summary")
        print("10. Exit")

        choice = input("Enter your choice: ").strip()

        if choice == "1":
            add_sale()
        elif choice == "2":
            view_sales()
        elif choice == "3":
            total_sales()
        elif choice == "4":
            average_sales()
        elif choice == "5":
            highest_lowest_sales()
        elif choice == "6":
            search_product()
        elif choice == "7":
            update_sale()
        elif choice == "8":
            delete_sale()
        elif choice == "9":
            sales_summary()
        elif choice == "10":
            print("Exiting program...")
            break
        else:
            print("Invalid choice. Please try again.")


if __name__ == "__main__":
    main()
