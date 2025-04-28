// filepath: e:\Project - 2025\PlacePrep\skillbridge\frontend\cypress\e2e\login.cy.js
describe('Login Page', () => {
  beforeEach(() => {
    // Visit the login page before each test
    cy.visit('/login');
  });

  it('should display login form', () => {
    // Check for login form elements
    cy.get('h2').should('contain', 'Sign in to your account');
    cy.get('input[type="email"]').should('be.visible');
    cy.get('input[type="password"]').should('be.visible');
    cy.get('button[type="submit"]').should('be.visible');
  });

  it('should navigate to register page', () => {
    // Click on the registration link
    cy.contains('Create an account').click();
    
    // Verify navigation to register page
    cy.url().should('include', '/register');
  });

  it('should show error for invalid credentials', () => {
    // Type incorrect credentials
    cy.get('input[type="email"]').type('wrong@example.com');
    cy.get('input[type="password"]').type('wrongpassword');
    
    // Submit the form
    cy.get('button[type="submit"]').click();
    
    // Check error message appears
    cy.get('.bg-red-100').should('be.visible');
    cy.get('.bg-red-100').should('contain', 'Invalid credentials');
  });

  it('should login successfully with valid credentials', () => {
    // Type valid credentials (using test account)
    cy.get('input[type="email"]').type('admin@skillbridge.com');
    cy.get('input[type="password"]').type('admin123');
    
    // Submit the form
    cy.get('button[type="submit"]').click();
    
    // Verify redirect to admin dashboard
    cy.url().should('include', '/admin/dashboard');
    cy.get('h1').should('contain', 'Dashboard');
  });
});