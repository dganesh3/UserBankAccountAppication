export function getLocalStorageItem(key: string): string | null {
  if (typeof window !== 'undefined' && localStorage) {
    const value = localStorage.getItem(key);
    console.log(`Getting localStorage[${key}] =>`, value);
    return value;
  }
  return null;
}

export function setLocalStorageItem(key: string, value: string): void {
  if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
    localStorage.setItem(key, value);
  }
}

export function removeLocalStorageItem(key: string): void {
  if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
    localStorage.removeItem(key);
  }
}