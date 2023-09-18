# Shopify♻️

Service for creating and managing any kind of orders.

# Requirements

 - Java 11
 - PostgreSQL
 - Docker

# Run

To run in a `Docker` use next follows:

```git
git clone https://github.com/SadCryFamily/Shopify.git
cd Shopify
./mvnw package -DskipTests
docker-compose build
docker-compose up -d
```
**Notice**

If you don't want to skip tests - create and setup the testable `PostgreSQL` before tests will be compeleted

```git
docker run -d -p 4455:5432 --name=postgres_test -e "POSTGRES_USER=postgres" -e "POSTGRES_PASSWORD=test" -e "POSTGRES_DB=test" postgres;
```

To run at `IDE` locally - clone repository to your local machine and open in IDE.

```git
git clone https://github.com/SadCryFamily/Shopify.git
```

# Features

1. **Adding Goods**

   Managers can effortlessly add new goods to the database, specifying product details such as name, price, and initial stock quantity.

2. **Listing Available Goods**

   Both managers and clients have access to a comprehensive list of available goods, complete with their prices and current stock quantities.

3. **Automatic Order Cleanup**

    To manage risks efficiently, the application automatically deletes unpaid orders ten minutes after their creation. This feature ensures that the database remains clutter-free and up-to-date.

4. **Payment Processing**

    Clients can easily pay for their orders through a dedicated endpoint, marking their orders as paid.

# Technologies

 - Java 11
 - PostgreSQL
 - Liquibase
 - Spring Data
 - Spring Boot
 - Spring Security
 - Docker

# Endpoints

Endpoints marked as `*` only authorized access only. Endpoints marked as `M` only Moderator can access.


`/signup` - create new Client.

`/signin` - login Client and provide JWT.

`M*/add-order` - create new Order.

`*/orders` - list of all available orders to buy.

`*/place-order` - set Order as customer property.

`*/pay-order` - pay already placed customer order.

`M*/moderation/clients` - get all customer with authorities.

`M*/moderation/grant` - grant specified customer with new role.

`M*/moderation/descent` - descent specified customer a moderator role.
