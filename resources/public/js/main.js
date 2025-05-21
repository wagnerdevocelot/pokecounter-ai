document.addEventListener('DOMContentLoaded', function() {
  // Initialize copy to clipboard functionality
  if (document.getElementById('copy-button')) {
    document.getElementById('copy-button').addEventListener('click', function() {
      const counterResults = document.getElementById('counter-results');

      // Create a temporary textarea element to copy from
      const textarea = document.createElement('textarea');
      textarea.value = counterResults.textContent;
      document.body.appendChild(textarea);

      // Select and copy the text
      textarea.select();
      document.execCommand('copy');

      // Remove the temporary textarea
      document.body.removeChild(textarea);

      // Update button text temporarily
      const originalText = this.textContent;
      this.textContent = 'Copied!';
      setTimeout(() => {
        this.textContent = originalText;
      }, 2000);
    });
  }

  // Initialize the form submission
  const counterForm = document.getElementById('counter-form');
  if (counterForm) {
    counterForm.addEventListener('submit', function(event) {
      // Validate the input (ensure there are no more than 6 Pokemon builds)
      const pokemonBuilds = document.getElementById('pokemon-builds').value;

      // Simple check: count the number of Pokemon entries (not perfect but sufficient for most cases)
      const pokemonCount = (pokemonBuilds.match(/^[A-Za-z][\w\-\s]+\s+@\s+/gm) || []).length;

      if (pokemonCount > 6) {
        event.preventDefault();
        alert('Please enter no more than 6 Pokemon builds.');
        return false;
      }

      // If validation passes, continue with the submission
      // Optionally, show a loading indicator
      document.querySelector('button[type="submit"]').disabled = true;
      document.querySelector('button[type="submit"]').innerHTML = 'Generating...';
    });
  }
});