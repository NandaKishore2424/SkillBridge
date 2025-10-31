# Contributing to SkillBridge

## Code of Conduct

### Our Pledge
We pledge to make participation in our project a harassment-free experience for everyone, regardless of age, body size, disability, ethnicity, gender identity and expression, level of experience, nationality, personal appearance, race, religion, or sexual identity and orientation.

### Our Standards
Examples of behavior that contributes to creating a positive environment include:
- Using welcoming and inclusive language
- Being respectful of differing viewpoints and experiences
- Gracefully accepting constructive criticism
- Focusing on what is best for the community
- Showing empathy towards other community members

## Getting Started

### Prerequisites
- Java 17
- Node.js 16+
- PostgreSQL
- Git

### Setting Up Development Environment
1. Fork the repository
2. Clone your fork:
   ```bash
   git clone https://github.com/your-username/SkillBridge.git
   ```
3. Set up upstream remote:
   ```bash
   git remote add upstream https://github.com/NandaKishore2424/SkillBridge.git
   ```

### Development Workflow
1. Create a new branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```
2. Make your changes
3. Write tests
4. Run tests:
   ```bash
   ./gradlew test    # Backend tests
   npm test         # Frontend tests
   ```
5. Commit your changes:
   ```bash
   git commit -m "feat: add your feature description"
   ```

## Coding Standards

### Java Code Style
- Follow Google Java Style Guide
- Use meaningful variable and method names
- Write comprehensive JavaDoc comments
- Keep methods focused and small
- Use proper exception handling

### TypeScript/JavaScript Code Style
- Follow Airbnb JavaScript Style Guide
- Use ES6+ features appropriately
- Use meaningful variable and function names
- Write JSDoc comments for complex functions
- Use proper error handling

### Commit Message Format
Follow the Conventional Commits specification:
```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

Types:
- feat: A new feature
- fix: A bug fix
- docs: Documentation changes
- style: Code style changes
- refactor: Code refactoring
- test: Adding or modifying tests
- chore: Changes to build process or tools

Example:
```
feat(auth): implement JWT refresh token mechanism

- Add refresh token generation
- Implement token refresh endpoint
- Add token validation middleware

Closes #123
```

## Pull Request Process

1. Update documentation to reflect changes
2. Add tests for new features
3. Ensure CI/CD pipeline passes
4. Update CHANGELOG.md
5. Request review from maintainers
6. Address review comments
7. Squash commits if requested

### PR Template
```markdown
## Description
[Describe the changes you've made]

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## How Has This Been Tested?
[Describe the tests you've run]

## Checklist
- [ ] My code follows the project's style guidelines
- [ ] I have performed a self-review
- [ ] I have commented my code where needed
- [ ] I have updated the documentation
- [ ] I have added tests that prove my fix/feature works
- [ ] All tests pass locally
```

## Testing Guidelines

### Backend Tests
- Write unit tests for services
- Write integration tests for controllers
- Test error conditions
- Aim for 80%+ coverage
- Use meaningful test names

### Frontend Tests
- Write component tests
- Test user interactions
- Test error states
- Use meaningful test descriptions
- Mock API calls appropriately

## Documentation

### Code Documentation
- Use descriptive comments
- Document public APIs
- Explain complex algorithms
- Include usage examples
- Document configuration options

### API Documentation
- Document all endpoints
- Include request/response examples
- Document error responses
- Keep documentation up to date
- Use OpenAPI/Swagger

## Review Process

### Code Review Guidelines
- Check code style
- Verify test coverage
- Review documentation
- Check performance impact
- Verify error handling
- Look for security issues

### Security Guidelines
- Validate inputs
- Sanitize outputs
- Use proper authentication
- Implement authorization
- Follow OWASP guidelines

## Release Process

### Version Numbers
Follow Semantic Versioning (MAJOR.MINOR.PATCH):
- MAJOR: Breaking changes
- MINOR: New features
- PATCH: Bug fixes

### Release Checklist
1. Update version number
2. Update CHANGELOG.md
3. Run all tests
4. Build documentation
5. Create release notes
6. Tag release
7. Deploy to staging
8. Verify deployment
9. Deploy to production

## Support

### Getting Help
- Check documentation
- Search existing issues
- Join Discord community
- Contact maintainers

### Reporting Issues
- Use issue templates
- Provide reproduction steps
- Include relevant logs
- Specify environment details
- Tag appropriately

## License
By contributing, you agree that your contributions will be licensed under the project's MIT License.