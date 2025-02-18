import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenService } from '../token/token.service';

export const httpTokenInterceptor: HttpInterceptorFn = (req, next) => {
	const authToken = inject(TokenService).token;

	if (authToken) {
		const newReq = req.clone({
			headers: req.headers.append('Authorization', `Bearer ${authToken}`)
		})
		return next(newReq);
	}
	return next(req);
};
