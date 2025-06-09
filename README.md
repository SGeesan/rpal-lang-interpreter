# RPAL Language Interpreter

> A comprehensive interpreter for the RPAL programming language, featuring lexical analysis, syntax parsing, AST/ST visualization, and semantic evaluation.

**Developed by Team Atlantis** 🌊

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Build Methods](#build-methods)
- [Examples](#examples)
- [Contributing](#contributing)
- [License](#license)

## 🔍 Overview

This RPAL interpreter is a complete implementation that processes RPAL (Right-reference Pedagogical Algorithmic Language) source code through multiple phases:

- **Lexical Analysis** - Tokenizes the input source code
- **Syntax Parsing** - Builds Abstract Syntax Trees (AST)
- **Tree Standardization** - Converts AST to Standardized Trees (ST)
- **Semantic Evaluation** - Executes the program and produces results

## ✨ Features

- 🔧 **Modular Architecture** - Clean separation of lexer, parser, and evaluator components
- 🌳 **Tree Visualization** - Optional AST and ST output for debugging and learning
- 🚀 **Multiple Build Options** - Support for both Maven and Makefile builds
- 📊 **Comprehensive Testing** - Includes test cases for validation
- 🎯 **Standards Compliant** - Full RPAL language specification support

## 📋 Prerequisites

- **Java JDK 8+** (tested with Java 21)
- **Maven 3.6+** (for Maven build method)
- **Make utility** (for Makefile build method)

## 🚀 Installation

### Method 1: Maven Build (Recommended)

```bash
# Clone the repository
git clone <repository-url>
cd rpal-lang-interpreter

# Clean and build the project
mvn clean install
```

This creates a JAR file in the `target/` directory.

### Method 2: Makefile Build

```bash
# Navigate to source directory
cd rpal/src/main/java

# Compile using make
make
```

This generates `.class` files in the same directory structure.

## 🎮 Usage

### Basic Execution

```bash
java myrpal <input_file>
```

### With Output Redirection

```bash
java myrpal test/test1 > output.txt
```

### Debug Options

Use optional switches to visualize internal tree structures:

| Switch | Description |
|--------|-------------|
| `-ast` | Print Abstract Syntax Tree |
| `-st`  | Print Standardized Tree |

### Examples

```bash
# Run basic interpretation
java myrpal test/test1

# View AST structure
java myrpal -ast test/test1

# View both AST and ST
java myrpal -ast -st test/test1

# Save output to file
java myrpal -ast test/test1 > debug_output.txt
```

## 📁 Project Structure

```
rpal-lang-interpreter/
├── src/
│   └── main/
│       └── java/
│           ├── lexer/          # Lexical analysis components
│           ├── parser/         # Syntax parsing and AST generation
│           ├── evaluator/      # Semantic evaluation engine
│           └── myrpal.java     # Main entry point
├── test/                       # Test cases and examples
├── target/                     # Maven build output
├── pom.xml                     # Maven configuration
├── Makefile                    # Alternative build configuration
└── README.md                   # This file
```

## 🔨 Build Methods

### Maven (Preferred)

Maven provides dependency management, testing integration, and packaging:

```bash
# Clean previous builds
mvn clean

# Compile and package
mvn compile package

# Run tests
mvn test

# Complete build with tests
mvn clean install
```

### Makefile (Alternative)

For environments where Maven isn't available or for submission requirements:

```bash
cd rpal/src/main/java
make clean  # Remove previous .class files
make        # Compile all source files
```

## 📝 Examples

### Sample RPAL Program

```rpal
let add x y = x + y
in add 5 3
```

### Running the Example

```bash
# Assuming the above code is saved as 'sample.rpal'
java myrpal sample.rpal
# Output: 8

# View the AST structure
java myrpal -ast sample.rpal
```

## 🧪 Testing

The project includes comprehensive test cases in the `test/` directory:

```bash
# Run all test cases using Makefile
make test
```

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is developed as part of an academic assignment. Please respect academic integrity guidelines when using this code.

---

## 🆘 Troubleshooting

### Common Issues

**Java version compatibility**: Ensure you're using JDK 8 or higher
```bash
java -version
```

**Maven not found**: Install Maven or use the Makefile alternative
```bash
mvn --version
```

**Class not found errors**: Ensure you're running from the correct directory after building

---

<div align="center">

**Made by Team Atlantis**

*Building bridges between theory and practice, one parse tree at a time.*

</div>