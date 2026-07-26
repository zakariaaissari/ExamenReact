import { isAxiosError } from 'axios'

// Spring's GlobalExceptionHandler returns { message, ... }. Surface that when
// present, otherwise fall back to the axios/network message.
export function errorMessage(err: unknown): string {
  if (isAxiosError(err)) {
    return err.response?.data?.message ?? err.message
  }
  return 'Something went wrong'
}
